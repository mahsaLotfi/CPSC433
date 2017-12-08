package ca.ucalgary.cpsc433.tree;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.environment.PartialAssign;
import ca.ucalgary.cpsc433.environment.Unwanted;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Implementation of the Or-Tree, specifically tuned for the class
 * scheduling problem. This generates a random schedule, with no guarantee
 * of returning the best schedule. Subsequent methods should be applied to
 * refine the solution from this search.
 * <p>
 * The implementation is thread-safe and can be used multiple times.
 *
 * @author Obicere
 */
public class OrTree {

    private final Environment environment;

    private final Slot[] lectureSlots;

    private final Slot[] labSlots;

    private final Lecture[] lectures;

    private final Lab[] labs;

    private final SecureRandom random;

    /**
     * Constructs a new OrTree solver. This can only solve solutions on the
     * given <code>environment</code>. This instance is threadsafe.
     *
     * @param environment The environment to solve for.
     */
    public OrTree(final Environment environment) {
        this.environment = environment;
        this.lectureSlots = environment.getLectureSlots();
        this.labSlots = environment.getLabSlots();
        this.lectures = environment.getLectures();
        this.labs = environment.getLabs();
        this.random = new SecureRandom();

        organizeCourses();
    }

    /**
     * Organizes the courses for optimal scheduling. The courses with more
     * constraints are prioritized. The order is:
     * <ol>
     * <li>Partial assignments</li>
     * <li>Evening courses</li>
     * <li>Unwanted assignments</li>
     * <li>500-level courses</li>
     * </ol>
     */
    private void organizeCourses() {
        int j = 0;
        // partial assignments have top priority
        for (int i = 0; i < lectures.length; i++) {
            if (environment.getPartialAssign(lectures[i]) != null) {
                swap(lectures, i, j);
                j++;
            }
            if(lectures[i].getNumber() == 313 || lectures[i].getNumber() == 313) {

            }
        }
        // evening, unwanted and 5XX lectures have second priority
        for (int i = 0; i < lectures.length; i++) {
            final int section = lectures[i].getSection();
            if (section >= 90) {
                swap(lectures, i, j);
                j++;
                continue;
            }

            if (environment.getUnwanted(lectures[i]).length > 0) {
                swap(lectures, i, j);
                j++;
                continue;
            }

            final int number = lectures[i].getNumber();
            if (number >= 500 && number < 600) {
                swap(lectures, i, j);
                j++;
                continue;
            }
        }

        j = 0;
        // partial assignments have top priority
        for (int i = 0; i < labs.length; i++) {
            if (environment.getPartialAssign(labs[i]) != null) {
                swap(labs, i, j);
                j++;
            }
        }
        // evening, unwanted and 5XX lectures have second priority
        for (int i = 0; i < labs.length; i++) {
            final int section = labs[i].getSection();
            if (section >= 90) {
                swap(labs, i, j);
                j++;
                continue;
            }

            if (environment.getUnwanted(labs[i]).length > 0) {
                swap(labs, i, j);
                j++;
                continue;
            }
        }
    }

    /**
     * Searches for a solution to the given problem, described by the
     * <code>environment</code>. There is no guarantee that the solution
     * provided is the optimal solution. Multiple searches should be
     * performed.
     * <p>
     * This method is threadsafe.
     *
     * @return <code>null</code> if there is no solution. Otherwise, the
     * {@link Schedule} solution is returned.
     */
    public Schedule search() {
        final Assign[] schedule = new Assign[environment.getCourseCount()];
        State current = new State(null, getSubtreeSize(0));
        int depth = 0;

        while (current != null) {
            // while there is non-solved tree, solve it
            while (!current.isComplete()) {
                // start on the next unsolved tree
                current.assign();

                final Course course = getCourse(depth);
                final Slot slot = getSlot(depth, current.current);

                // System.out.println(depth + ": Assigning " + course + " to " + slot);
                schedule[depth] = Assign.getAssign(course, slot);
                depth++;

                Schedule build = build(depth, schedule);
                if (build.isValid()) {
                    if (isScheduleComplete(depth)) {
                        // System.out.println("Found valid solution");
                        return build;
                    } else {
                        current = new State(current, getSubtreeSize(depth));
                        // filter the new state to remove and obvious solutions
                        filter(getCourse(depth), current);
                    }
                } else {
                    depth--;
                    current.mark();
                }
            }
            // System.out.println("Going back");
            current = goBack(current);
            depth--;
        }
        return null;
    }

    /**
     * Goes back a state. Does not decrement the depth.
     *
     * @param current The state to reverse.
     * @return The previous state.
     */
    private State goBack(State current) {
        current = current.previous;
        if (current != null) {
            current.mark();
            return current;
        } else {
            return null;
        }
    }

    /**
     * Retrieves the course based on the given index. The order may not be
     * the same as the original sequence, so this is necessary to preserve
     * integrity.
     *
     * @param i The index of the course to get.
     * @return The course at the index.
     */
    private Course getCourse(final int i) {
        if (i < lectures.length) {
            return lectures[i];
        } else {
            return labs[i - lectures.length];
        }
    }

    /**
     * Retrieves the subtree size based on the depth. The depth is used to
     * determine if a lecture or a lab is being considered.
     *
     * @param depth The current depth of the tree.
     * @return The number of subtrees.
     */
    private int getSubtreeSize(final int depth) {
        if (depth < lectures.length) {
            return lectureSlots.length;
        } else {
            return labSlots.length;
        }
    }

    /**
     * Retrieves the given slot based on the depth and index. The depth is
     * used to determine if a lecture or a lab is being considered.
     *
     * @param depth The current depth of the tree.
     * @param i     The index of the slot.
     * @return The slot at the index.
     */
    private Slot getSlot(final int depth, final int i) {
        if (depth < lectures.length) {
            return lectureSlots[i];
        } else {
            return labSlots[i];
        }
    }

    /**
     * Whether or not the given depth indicates all courses have been
     * assigned.
     *
     * @param depth THe depth to consider.
     * @return <code>true</code> iff all courses are assigned.
     */
    private boolean isScheduleComplete(final int depth) {
        return depth == lectures.length + labs.length;
    }

    /**
     * Builds a schedule given the list of assignments and current depth of
     * the tree. Only elements up to the depth can be included in the
     * schedule.
     *
     * @param depth    The depth of the tree (how many assignments are
     *                 solved)
     * @param schedule The list of current assignments.
     * @return The schedule built from the given assignments and depth.
     */
    private Schedule build(final int depth, final Assign[] schedule) {
        final Assign[] sub;
        if (isScheduleComplete(depth)) {
            sub = schedule;
        } else {
            sub = Arrays.copyOf(schedule, depth);
        }
        return new Schedule(environment, sub);
    }

    /**
     * Swaps two objects in an array. Assumes that the bounds are valid.
     * Order of indices does not matter.
     *
     * @param objects The array to swap in.
     * @param i       First index.
     * @param j       Second index.
     */
    private void swap(final Object[] objects, final int i, final int j) {
        final Object temp = objects[i];
        objects[i] = objects[j];
        objects[j] = temp;
    }

    /**
     * Filters the possible slot assignments that can be made, given a
     * specific state and course. As of now, this filters based on:
     * <ul>
     * <li>Evening courses must be assigned to evening slots</li>
     * <li>Partial assignments only have 1 possible solution</li>
     * <li>Unwanted assignments are not possible solutions</li>
     * </ul>
     *
     * @param course The course to consider.
     * @param state  The state to filter the subtrees.
     */
    private void filter(final Course course, final State state) {
        final Slot[] slots;
        if (course.isLecture()) {
            slots = lectureSlots;
        } else {
            slots = labSlots;
        }
        if (course.getSection() >= 90) {
            for (int i = 0; i < slots.length; i++) {
                if (!slots[i].getTime().isEvening()) {
                    state.mark(i);
                }
            }
        }
        if (environment.getPartialAssign(course) != null) {
            final PartialAssign assign = environment.getPartialAssign(course);
            final Slot s = assign.getAssign().getSlot();
            for (int i = 0; i < slots.length; i++) {
                if (!slots[i].equals(s)) {
                    state.mark(i);
                }
            }
        }
        final Unwanted[] unwanted = environment.getUnwanted(course);
        if (unwanted.length > 0) {
            for (final Unwanted un : unwanted) {
                state.mark(un.getAssign().getSlot().getSlotID());
            }
        }
    }

    /**
     * Stores the current state. Subtrees can be marked as solved. Once all
     * subtrees are solved, then the state is complete.
     */
    private class State {

        private int current = -1;

        private final boolean[] subTrees;

        private int unchecked;

        private State previous;

        /**
         * Create a new state, linking it to the previous.
         *
         * @param previous     previous state.
         * @param subTreeCount How many subtrees to consider.
         */
        private State(final State previous, final int subTreeCount) {
            this.subTrees = new boolean[subTreeCount];
            this.unchecked = subTreeCount;

            this.previous = previous;
        }

        /**
         * Marks the current subtree as solved. Requires a call to
         * <code>assign</code> previous to this execution.
         */
        void mark() {
            this.subTrees[current] = true;
            unchecked--;
        }

        /**
         * Marks the subtree as solved. Mostly used for filtering subtrees,
         * where it is known that solutions cannot exist.
         *
         * @param i the subtree to mark solved.
         */
        void mark(final int i) {
            this.subTrees[i] = true;
            unchecked--;
        }

        /**
         * Whether or not the current state is complete. This occurs when
         * all subtrees are solved.
         *
         * @return <code>true</code> iff all subtrees are marked solved.
         */
        boolean isComplete() {
            return (unchecked == 0);
        }

        /**
         * Current subtree being considered.
         *
         * @return the current subtree being considered.
         */
        int getCurrent() {
            return current;
        }

        /**
         * Retrieve a "random" subtree that hasn't been checked yet. This
         * will select a random tree, then find the next possible subtree
         * that is unsolved.
         */
        void assign() {
            int next = (int) (subTrees.length * random.nextDouble());
            while (subTrees[next]) {
                next = (next + 1) % subTrees.length;
            }
            this.current = next;
        }

    }

}
