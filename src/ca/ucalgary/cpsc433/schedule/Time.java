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

    public boolean isEvening() {
        return hour >= 18;
    }

    public boolean isInRange(final Time start, final Time end) {
        final int compareStart = start.compareTo(this);
        final int compareEnd = end.compareTo(this);

        return compareStart <= 0 && compareEnd >= 0;
    }

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
                case 1830:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public Time add(final int hours, final int minutes) {
        final int mMod = (minute + minutes) % 60;
        final int mDiv = (minute + minutes) / 60;
        final int hMod = (hour + hours + mDiv) % 24;
        return new Time(hMod, mMod);
    }

    private int formatInt() {
        // outputs the time in HHMM format in decimal
        return hour * 100 + minute;
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
