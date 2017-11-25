package ca.ucalgary.cpsc433.tree;

import ca.ucalgary.cpsc433.schedule.Assign;

/**
 * @author Obicere
 */
public class OrTree {

    // this needs to be really memory efficient
    // state will take care of this. No Schedule objects should be used until the final schedule is procurred.

    private class State {

        private final Assign assign;

        private final boolean[] subTrees;

        private int unchecked;

        private State(final Assign assign, final int subTreeCount) {
            this.assign = assign;
            this.subTrees = new boolean[subTreeCount];
            this.unchecked = subTreeCount;
        }

        void mark(final int subTree) {
            this.subTrees[subTree] = true;
            unchecked--;
        }

        boolean isComplete() {
            return unchecked == 0;
        }

        /**
         * Retrieve a "random" subtree that hasn't been checked yet. This
         * will select a random tree, then find the next possible subtree
         * that is unsolved.
         *
         * @return the next unsolved subtree.
         */
        int getNext() {
            int next = (int) (subTrees.length * Math.random());
            while (subTrees[next]) {
                next = (next + 1) % subTrees.length;
            }
            return next;
        }

    }

}
