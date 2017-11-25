package ca.ucalgary.cpsc433.schedule;

/**
 * @author Obicere
 */
public class Time {

    // format: "HH:MM"
    private final String time;

    public Time(final String time) {
        if (time == null) {
            throw new NullPointerException("time must be non-null.");
        }
        this.time = time;
    }

    public boolean isAfter1800() {
        // example of why this class could be useful

        // TODO
        return false;
    }

    @Override
    public String toString() {
        return time;
    }
}
