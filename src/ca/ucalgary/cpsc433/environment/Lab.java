package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class Lab implements Course {

    public Lecture getLecture() {
        // TODO
        return null;
    }

    @Override
    public int getNumber() {
        return -1;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int compareTo(final Course o) {
        if (o instanceof Lecture) {
            return 1;
        }
        // TODO this might be changed later on
        return Integer.compare(getNumber(), o.getNumber());
    }
}
