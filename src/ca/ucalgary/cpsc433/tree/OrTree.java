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
 * @author Obicere
 */
public class OrTree {

    // this needs to be really memory efficient
    // state will take care of this. No Schedule objects should be used until the final schedule is procurred.

    private final Environment environment;

    private final Slot[] lectureSlots;

    private final Slot[] labSlots;

    private final Lecture[] lectures;

    private final Lab[] labs;

    private final SecureRandom random;

    private int depth = 0;

    private Assign[] schedule;

    public OrTree(final Environment environment) {
        this.environment = environment;
        this.lectureSlots = environment.getLectureSlots();
        this.labSlots = environment.getLabSlots();
        this.lectures = environment.getLectures();
        this.labs = environment.getLabs();
        this.random = new SecureRandom();

        this.schedule = new Assign[lectures.length + labs.length];
    }

    public Schedule search() {
        State current = new State(null, getSubtreeSize(0));

        int j = 0;
        for (int i = 0; i < lectures.length; i++) {
            if (environment.getPartialAssign(lectures[i]) != null) {
                swap(lectures, i, j);
                j++;
                continue;
            }

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
        for (int i = 0; i < labs.length; i++) {
            if (environment.getPartialAssign(labs[i]) != null) {
                swap(labs, i, j);
                j++;
                continue;
            }

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

        while (current != null) {
            while (!current.isComplete()) {
                current.assign();

                final Course course = getCourse(depth);
                final Slot slot = getSlot(depth, current.current);

                // System.out.println(depth + ": Assigning " + course + " to " + slot);
                schedule[depth] = Assign.getAssign(course, slot);
                depth++;

                Schedule build = build();
                if (build.isValid()) {
                    if (isComplete()) {
                        // System.out.println("Found valid solution");
                        return build;
                    } else {
                        current = new State(current, getSubtreeSize(depth));
                        filter(getCourse(depth), current);
                    }
                } else {
                    depth--;
                    current.mark();
                }
            }
            // System.out.println("Going back");
            current = goBack(current);
        }
        return null;
    }

    private State goBack(State current) {
        current = current.previous;
        depth--;
        if (current != null) {
            current.mark();
            return current;
        } else {
            return null;
        }
    }

    private Course getCourse(final int i) {
        if (i < lectures.length) {
            return lectures[i];
        } else {
            return labs[i - lectures.length];
        }
    }

    private int getSubtreeSize(final int i) {
        if (i < lectures.length) {
            return lectureSlots.length;
        } else {
            return labSlots.length;
        }
    }

    private Slot getSlot(final int depth, final int i) {
        if (depth < lectures.length) {
            return lectureSlots[i];
        } else {
            return labSlots[i];
        }
    }

    private boolean isComplete() {
        return depth == lectures.length + labs.length;
    }

    private Schedule build() {
        final Assign[] sub;
        if (isComplete()) {
            sub = schedule;
        } else {
            sub = Arrays.copyOf(schedule, depth);
        }
        return new Schedule(environment, sub);
    }

    private void swap(final Object[] objects, final int i, final int j) {
        final Object temp = objects[i];
        objects[i] = objects[j];
        objects[j] = temp;
    }

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

    private class State {

        private int current = -1;

        private final boolean[] subTrees;

        private int unchecked;

        private State next;

        private State previous;

        private State(final State previous, final int subTreeCount) {
            this.subTrees = new boolean[subTreeCount];
            this.unchecked = subTreeCount;

            this.previous = previous;
            if (previous != null) {
                previous.next = this;
            }
        }

        State getPrevious() {
            return previous;
        }

        State getNext() {
            return next;
        }

        void mark() {
            this.subTrees[current] = true;
            unchecked--;
        }

        void mark(int i) {
            this.subTrees[i] = true;
            unchecked--;
        }

        boolean isComplete() {
            return (unchecked == 0);
        }

        int getCurrent() {
            return current;
        }

        /**
         * Retrieve a "random" subtree that hasn't been checked yet. This
         * will select a random tree, then find the next possible subtree
         * that is unsolved.
         *
         * @return the next unsolved subtree.
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
