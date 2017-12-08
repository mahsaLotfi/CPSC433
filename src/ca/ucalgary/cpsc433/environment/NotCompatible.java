package ca.ucalgary.cpsc433.environment;

/**
 * One of the general hard constraints is NotCompatible which contains 
 * 2 courses that are non-compatible. 
 * 
 * @author Obicere
 */
public class NotCompatible {

    private final Course left;

    private final Course right;

    /**
     * Constructor to initialize each not compatible courses.
     * @param left
     * @param right
     */
    public NotCompatible(final Course left, final Course right) {
        if (left == null) {
            throw new NullPointerException("left must be non-null.");
        }
        if (right == null) {
            throw new NullPointerException("right must be non-null.");
        }
        this.left = left;
        this.right = right;
    }

    /**
     * Getters
     * @return Course
     */
    
    public Course getLeft() {
        return left;
    }

    public Course getRight() {
        return right;
    }

    /**
     * Returns hash code value of left course + right course
     * @return int
     */
    @Override
    public int hashCode() {
        return left.hashCode() * 31 + right.hashCode();
    }

    /**
     * Returns true if the argument's left & right courses are 
     * equal to the current object's. False if not.
     * @return boolean
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof NotCompatible)) {
            return false;
        }
        final NotCompatible other = (NotCompatible) o;
        return getLeft().equals(other.getLeft()) && getRight().equals(other.getRight());
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "NotCompatible[" + left + ", " + right + "]";
    }
}
