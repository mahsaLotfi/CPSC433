package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class Lecture implements Course {

    public Lab[] getLabs() {
        // TODO
        return null;
    }

    @Override
    public int getNumber() {
        return -1;
    }

    @Override
    public String getName() {
        // TODO
        return null;
    }

    @Override
    public int compareTo(final Course o) {
        if(o instanceof Lab) {
            return -1;
        }
        // TODO this might be changed later on
        return Integer.compare(getNumber(), o.getNumber());
    }
}
