package ca.ucalgary.cpsc433.environment;

import ca.ucalgary.cpsc433.schedule.Assign;

/**
 * @author Obicere
 */
public class Unwanted {

    private final Assign assign;

    public Unwanted(final Assign assign) {
        if (assign == null) {
            throw new NullPointerException("assign must be non-null.");
        }
        this.assign = assign;
    }

    public Assign getAssign() {
        return assign;
    }

    @Override
    public int hashCode() {
        return assign.hashCode();
    }

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

    @Override
    public String toString() {
        return "Unwanted[" + assign + "]";
    }

}
