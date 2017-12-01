package ca.ucalgary.cpsc433.schedule;

/**
 * @author Obicere
 */
public class Slot {

    private final Day day;

    private final Time time;

    private final int max;

    private final int min;

    public Slot(final Day day, final Time time) {
        this(day, time, 0, 0);
    }

    public Slot(final Day day, final Time time, final int max, final int min) {
        this.day = day;
        this.time = time;
        this.max = max;
        this.min = min;
    }

    public Day getDay() {
        return day;
    }

    public Time getTime() {
        return time;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    @Override
    public int hashCode() {
        return 31 * day.hashCode() + time.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Slot)) {
            return false;
        }
        final Slot other = (Slot) o;
        return getDay().equals(other.getDay()) && getTime().equals(other.getTime());
    }

    @Override
    public String toString() {
        return day + ", " + time;
    }
}
