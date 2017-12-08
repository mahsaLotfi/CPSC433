package ca.ucalgary.cpsc433.environment;

/**
 * This is a class for Pair courses. One of the general soft constraints are
 * courses that are pair together, which department knows that students will
 * not get these courses together. So it's ok to assign these courses at the
 * same time.  
 * 
 * @author Obicere
 */
public class Pair {

    private final Course left;

    private final Course right;

    /**
     * Constructor to initialize each pair courses.
     * @param left
     * @param right
     */
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
     * equal to the current object. False if not.
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
        if (!(o instanceof Pair)) {
            return false;
        }
        final Pair other = (Pair) o;
        return getLeft().equals(other.getLeft()) && getRight().equals(other.getRight());
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Pair[" + left + ", " + right + "]";
    }
}
