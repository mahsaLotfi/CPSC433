package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class NotCompatible {

    private final Course left;

    private final Course right;

    public NotCompatible(final Course left, final Course right) {
        if (left == null) {
            throw new NullPointerException("left must be non-null.");
        }
        if (right == null) {
            throw new NullPointerException("right must be non-null.");
        }

        final int cmp = left.compareTo(right);
        // insert the elements into the correct order, halves the number of instances
        if (cmp <= 0) {
            this.left = left;
            this.right = right;
        } else {
            this.right = left;
            this.left = right;
        }
    }

    /**
     * Apply transitivity. If <code>a</code> is not compatible with
     * <code>b</code>, and <code>b</code> is not compatible with
     * <code>c</code>, then <code>a</code> is not compatible with
     * <code>c</code>.
     *
     * @param other The other non-compatibility to extend with.
     * @return <code>null</code> if transitivity does not apply. Otherwise,
     * the tuple storing the transitive elements.
     */
    public NotCompatible extend(final NotCompatible other) {
        final Course otherLeft = other.left;
        final Course otherRight = other.right;

        if (otherLeft.equals(right)) {
            return new NotCompatible(left, otherRight);
        }

        if (otherRight.equals(left)) {
            return new NotCompatible(otherLeft, right);
        }

        return null;
    }

}
