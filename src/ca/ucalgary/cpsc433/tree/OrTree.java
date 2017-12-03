package ca.ucalgary.cpsc433.tree;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.environment.PartialAssign;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Day;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.schedule.Time;

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

    private final PartialAssign[] partialAssign;

    private final SecureRandom random;

    private int depth = 0;

    private Assign[] schedule;

    public OrTree(final Environment environment) {
        this.environment = environment;
        this.lectureSlots = environment.getLectureSlots();
        this.labSlots = environment.getLabSlots();
        this.lectures = environment.getLectures();
        this.labs = environment.getLabs();
        this.partialAssign = environment.getPartialAssigns();
        this.random = new SecureRandom();

        this.schedule = new Assign[lectures.length + labs.length];
    }

    public Schedule search() {
        final int badSlot = findTuesday1230Slot();
        final int n13Slot = findTuesday1800Slot();

        final int[] slot500 = new int[get500Count()];
        Arrays.fill(slot500, -1);

        State current = new State(null, getSubtreeSize(0));

        depth++;

        while (current != null) {
            current.assign();

            final Course course = getCourse(depth - 1);
            final Slot slot = getSlot(depth - 1, current.current);

            // System.out.println((depth - 1) + ": Assigning " + course + " to " + slot);
            schedule[depth - 1] = Assign.getAssign(course, slot);

            final Schedule build = build();
            if (build.isValid()) {
                if (isComplete()) {
                    // System.out.println("Found valid solution");
                    return build;
                }
            }
            if (!build.isValid() || current.isComplete()) {
                // System.out.println("Found invalid solution");
                current = current.previous;
                depth--;
                current.mark();
            }

            final int subCount = getSubtreeSize(depth);
            current = new State(current, subCount);

            depth++;
        }

        return null;
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

    private int get500Count() {
        int count = 0;
        for (final Lecture lecture : lectures) {
            if (lecture.getNumber() >= 500 && lecture.getNumber() < 600) {
                count++;
            }
        }
        return count;
    }

    private int findTuesday1230Slot() {
        final Time time = new Time(12, 30);
        for (int i = 0; i < lectureSlots.length; i++) {
            if (lectureSlots[i].getDay() == Day.TUESDAY && lectureSlots[i].getTime().equals(time)) {
                return i;
            }
        }
        return -1;
    }

    private int findTuesday1800Slot() {
        final Time time = new Time(18, 0);
        for (int i = 0; i < lectureSlots.length; i++) {
            if (lectureSlots[i].getDay() == Day.TUESDAY && lectureSlots[i].getTime().equals(time)) {
                return i;
            }
        }
        return -1;
    }

    private int findSlot(final Slot slot, final Slot[] slots) {
        // TODO could be cached
        for (int i = 0; i < slots.length; i++) {
            if (slot.equals(slots[i])) {
                return i;
            }
        }
        return -1;
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
