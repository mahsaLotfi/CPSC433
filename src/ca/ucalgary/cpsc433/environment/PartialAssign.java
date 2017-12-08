package ca.ucalgary.cpsc433.environment;

import ca.ucalgary.cpsc433.schedule.Assign;

/**
 * One of the general Hard constraints is Partial assignment.
 * partialAssign: Courses + Labs -> Slots + {$}. The assignment assign has to fulfill the condition: 
 * assign(a) = partialAssign(a) for all a in Courses + Labs with partialAssign(a) not equal to $.
 * 
 * @author Obicere
 */
public class PartialAssign {

    private final Assign assign;

    /**
     * Constructor to initialize assign.
     * @param assign
     */
    public PartialAssign(final Assign assign) {
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
        if (!(o instanceof PartialAssign)) {
            return false;
        }
        return assign.equals(((PartialAssign) o).getAssign());
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "PartialAssign[" + assign + "]";
    }
}
