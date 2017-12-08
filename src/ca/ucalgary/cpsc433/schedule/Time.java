package ca.ucalgary.cpsc433.schedule;

/**
 * Time based on a 24 hour clock. Hour ranges from 0-23 and minutes range from 0-59.
 * Can be compared with other instances of Time.
 */
public class Time implements Comparable<Time> {

    private final int hour;

    private final int minute;

    /**
     * Constructor to initialize hour and minute variables
     * @param hour in 24hr clock
     * @param minute in range of 0-59 minutes
     */
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

    /**
     * @return hour in range of 0-23
     */
    public int getHour() {
        return hour;
    }

    /**
     * @return minute in range of 0-59
     */
    public int getMinute() {
        return minute;
    }

    /**
     * @return true if hour is >= 1800
     */
    public boolean isEvening() {
        return hour >= 18;
    }

    /**
     * Determines if two different times overlap
     * @param start Time to be compared
     * @param end Time to be compared
     * @return true if the two time slots overlap
     */
    public boolean isInRange(final Time start, final Time end) {
        final int compareStart = start.compareTo(this);
        final int compareEnd = end.compareTo(this);

        return compareStart <= 0 && compareEnd >= 0;
    }

    /**
     * Checks if a lab time is in a valid slot
     * @param day the day the lab slot is on
     * @return true if it is a valid day and time 
     */
    public boolean isValidLabTime(final Day day) {
        if (day == Day.MONDAY || day == Day.TUESDAY) {
            switch (formatInt()) {
                case 800:
                case 900:
                case 1000:
                case 1100:
                case 1200:
                case 1300:
                case 1400:
                case 1500:
                case 1600:
                case 1700:
                case 1800:
                case 1900:
                case 2000:
                    return true;
                default:
                    return false;
            }
        } else if (day == Day.FRIDAY) {
            switch (formatInt()) {
                case 800:
                case 1000:
                case 1200:
                case 1400:
                case 1600:
                case 1800:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if the lecture time is valid
     * @param day lecture slot
     * @return true if the day and time combo is valid
     */
    public boolean isValidLectureTime(final Day day) {
        if (day == Day.MONDAY) {
            switch (formatInt()) {
                case 800:
                case 900:
                case 1000:
                case 1100:
                case 1200:
                case 1300:
                case 1400:
                case 1500:
                case 1600:
                case 1700:
                case 1800:
                case 1900:
                case 2000:
                    return true;
                default:
                    return false;
            }
        } else if (day == Day.TUESDAY) {
            switch (formatInt()) {
                case 800:
                case 930:
                case 1100:
                case 1230:
                case 1400:
                case 1530:
                case 1700:
                case 1800:
                case 1830:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Adds the specified time to its Time
     * @param hours hours to be added
     * @param minutes minutes to be added
     * @return Time the new values for hour and minute
     */
    public Time add(final int hours, final int minutes) {
        final int mMod = (minute + minutes) % 60;
        final int mDiv = (minute + minutes) / 60;
        final int hMod = (hour + hours + mDiv) % 24;
        return new Time(hMod, mMod);
    }

    /**
     * Formats the time 
     * @return new formated time in HHMM
     */
    private int formatInt() {
        // outputs the time in HHMM format in decimal
        return hour * 100 + minute;
    }

    /**
     * @return the hash code, time converted to minutes
     */
    public int hashCode() {
        return hour * 60 + minute;
    }

    /** 
     * Checks if the Time of an object and this are equal
     * @param o Object to be compared
     * @return true if this.hour and this.minute equals the object being compared 
     */
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

    /**
     * @return Time in string format
     */
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    /**
     * Compares the Time if it is either > < or =
     * @param o Time to be compared to
     * @return -1 if less than, 1 if greater than, otherwise 0
     */
    public int compareTo(final Time o) {
        final int hourCompare = Integer.compare(getHour(), o.getHour());
        if (hourCompare != 0) {
            return hourCompare;
        } else {
            return Integer.compare(getMinute(), o.getMinute());
        }
    }
}
