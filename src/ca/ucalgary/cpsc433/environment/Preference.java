package ca.ucalgary.cpsc433.environment;

import ca.ucalgary.cpsc433.schedule.Assign;

/**
 * @author Obicere
 */
public class Preference {

    private final Assign assign;

    private final int value;

    public Preference(final Assign assign, final int value) {
        if (assign == null) {
            throw new NullPointerException("assign must be non-null.");
        }
        this.assign = assign;
        this.value = value;
    }

    public Assign getAssign() {
        return assign;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return assign.hashCode() * 31 + value;
    }

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

    @Override
    public String toString() {
        return "Preference[" + assign + ", " + value + "]";
    }

}
