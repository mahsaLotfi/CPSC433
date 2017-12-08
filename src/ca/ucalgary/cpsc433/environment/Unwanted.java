package ca.ucalgary.cpsc433.environment;

import ca.ucalgary.cpsc433.schedule.Assign;

/**
 * One of the general soft constraints is Unwanted Slots which
 * there is a certain pressure to also put courses and labs into the more unwanted slots. 
 * 
 * 
 * @author Obicere
 */
public class Unwanted {

    private final Assign assign;

    /**
     * Constructor to initialize assign.
     * @param assign
     */
    public Unwanted(final Assign assign) {
        if (assign == null) {
            throw new NullPointerException("assign must be non-null.");
        }
        this.assign = assign;
    }

    /**
     * Getter
     * @return Assign
     */
    public Assign getAssign() {
        return assign;
    }

    /**
     * Returns hash code value of assign
     * @return int
     */
    @Override
    public int hashCode() {
        return assign.hashCode();
    }

    /**
     * Returns true if the argument's assign is 
     * equal to the current object's assign. False if not.
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
        if (!(o instanceof Unwanted)) {
            return false;
        }
        return assign.equals(((Unwanted) o).getAssign());
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Unwanted[" + assign + "]";
    }

}
