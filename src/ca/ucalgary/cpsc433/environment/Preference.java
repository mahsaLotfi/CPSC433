package ca.ucalgary.cpsc433.environment;

import ca.ucalgary.cpsc433.schedule.Assign;

/**
 * One of the general soft constraints is Preference of certain Professors. 
 * 
 * @author Obicere
 */
public class Preference {

    private final Assign assign;

    private final int value;

    /**
     * Constructor to initialize assign and value.
     * @param assign
     */
    public Preference(final Assign assign, final int value) {
        if (assign == null) {
            throw new NullPointerException("assign must be non-null.");
        }
        this.assign = assign;
        this.value = value;
    }

    /**
     * Getter
     * @return Assign
     */
    public Assign getAssign() {
        return assign;
    }

    /**
     * Getter
     * @return int value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns hash code value of assign + value
     * @return int
     */
    @Override
    public int hashCode() {
        return assign.hashCode() * 31 + value;
    }

    /**
     * Returns true if the argument's assign & value are
     * equal to the current object's assign & value. False if not.
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
        if (!(o instanceof Preference)) {
            return false;
        }
        final Preference other = (Preference) o;
        return getAssign().equals(other.getAssign()) && getValue() == other.getValue();
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Preference[" + assign + ", " + value + "]";
    }

}
