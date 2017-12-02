package ca.ucalgary.cpsc433.tree;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.environment.NotCompatible;
import ca.ucalgary.cpsc433.environment.Pair;
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

    private State root;

    private OrTree(final Environment environment) {
        this.environment = environment;
        this.lectureSlots = environment.getLectureSlots();
        this.labSlots = environment.getLabSlots();
        this.lectures = environment.getLectures();
        this.labs = environment.getLabs();
        this.partialAssign = environment.getPartialAssigns();
        this.random = new SecureRandom();
    }

    public Schedule search() {

    }

    private void searchLecture() {
        final int badSlot = findTuesday1230Slot();
        final int n13Slot = findTuesday1800Slot();

        final int[] slot500 = new int[get500Count()];
        Arrays.fill(slot500, -1);


    }

    private void searchLab() {

    }

    private boolean isComplete() {
        return depth == lectures.length + labs.length;
    }

    private Schedule build() {
        final Assign[] assigns = new Assign[lectures.length + labs.length];

        State state = root;
        for (int i = 0; i < lectures.length; i++) {
            assigns[i] = new Assign(lectures[i], lectureSlots[state.current]);
            state = state.next;
        }

        for (int i = 0; i < labs.length; i++) {
            assigns[i + lectures.length] = new Assign(labs[i], labSlots[state.current]);
            state = state.next;
        }

        if (state != null) {
            throw new AssertionError("state must be null at this point.");
        }
        return new Schedule(environment, assigns);
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

        private State(final State previous, final int subTreeCount) {
            this.subTrees = new boolean[subTreeCount];
            this.unchecked = subTreeCount;

            if (previous != null) {
                previous.next = this;
            }
        }

        State getNext() {
            return next;
        }

        void mark(final int subTree) {
            this.subTrees[subTree] = true;
            unchecked--;
        }

        boolean isComplete() {
            return unchecked == 0;
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
        int getNextAssign() {
            int next = (int) (subTrees.length * random.nextDouble());
            while (subTrees[next]) {
                next = (next + 1) % subTrees.length;
            }
            this.current = next;
            return next;
        }

    }

}
