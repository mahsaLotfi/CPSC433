package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class Pair {

    private final Course left;

    private final Course right;

    public Pair(final Course left, final Course right) {
        if (left == null) {
            throw new NullPointerException("left must be non-null.");
        }
        if (right == null) {
            throw new NullPointerException("right must be non-null.");
        }
        this.left = left;
        this.right = right;
    }

    public Course getLeft() {
        return left;
    }

    public Course getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return left.hashCode() * 31 + right.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        final Pair other = (Pair) o;
        return getLeft().equals(other.getLeft()) && getRight().equals(other.getRight());
    }

    @Override
    public String toString() {
        return "Pair[" + left + ", " + right + "]";
    }
}
