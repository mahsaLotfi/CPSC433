package ca.ucalgary.cpsc433.schedule;

/**
 * @author Obicere
 */
public class Time implements Comparable<Time> {

    private final int hour;

    private final int minute;

    public Time(final int hour, final int minute) {
        if (hour < 0 || hour >= 24) {
            throw new IllegalArgumentException("hour must be in range [0, 23]: " + hour);
        }
        if (minute < 0 || minute >= 60) {
            throw new IllegalArgumentException("minute must be in range [0, 59]: " + minute);
        }
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isAfter1800() {
        return hour >= 18;
    }

    @Override
    public int hashCode() {
        return hour * 60 + minute;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Time)) {
            return false;
        }
        final Time other = (Time) o;
        return other.getHour() == getHour() && other.getMinute() == getMinute();
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    @Override
    public int compareTo(final Time o) {
        final int hourCompare = Integer.compare(getHour(), o.getHour());
        if (hourCompare != 0) {
            return hourCompare;
        } else {
            return Integer.compare(getMinute(), o.getMinute());
        }
    }
}
