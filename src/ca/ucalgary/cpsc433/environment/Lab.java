package ca.ucalgary.cpsc433.environment;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Obicere
 */
public class Lab implements Course {

    private static final Map<String, Lab> CACHE = new ConcurrentHashMap<>();

    private static int labCount = 0;

    private final int labID;

    private final Lecture[] lecture;

    private final boolean isLab;

    private final int section;

    private final String type;

    private final int number;

    public static Lab getLab(final Lecture[] lecture, final boolean isLab, final int section) {
        final String id = getCacheID(lecture[0].getType(), lecture[0].getNumber(), section);
        final Lab cached = CACHE.get(id);
        if (cached != null) {
            return cached;
        }
        final Lab newLab = new Lab(lecture, isLab, section);
        CACHE.put(id, newLab);
        return newLab;
    }

    public static int getLabCount() {
        return labCount;
    }

    public static Lab[] getLabs() {
        final Lab[] labs = new Lab[labCount];
        final Collection<Lab> values = CACHE.values();
        for (final Lab l : values) {
            labs[l.getID()] = l;
        }
        return labs;
    }

    private static String getCacheID(final String type, final int number, final int section) {
        return type + number + section;
    }

    Lab(final Lecture[] lecture, final boolean isLab, final int section) {
        if (lecture == null) {
            throw new NullPointerException("lecture must be non-null");
        }
        this.type = lecture[0].getType();
        this.number = lecture[0].getNumber();
        this.lecture = lecture;
        this.isLab = isLab;
        this.section = section;

        this.labID = labCount++;
    }

    @Override
    public int getID() {
        return labID;
    }

    public Lecture[] getLectures() {
        return lecture;
    }

    public boolean isLab() {
        return isLab;
    }

    @Override
    public int getNumber() {
        return lecture[0].getNumber();
    }

    @Override
    public String getType() {
        return lecture[0].getType();
    }

    @Override
    public int getSection() {
        return section;
    }

    @Override
    public boolean isLecture() {
        return false;
    }

    @Override
    public int compareTo(final Course o) {
        if (o instanceof Lecture) {
            return 1;
        }
        final Lab other = (Lab) o;
        final int numberCmp = Integer.compare(getNumber(), other.getNumber());
        if (numberCmp != 0) {
            return numberCmp;
        }
        final int sectionCmp = Integer.compare(getSection(), other.getSection());
        if (sectionCmp != 0) {
            return sectionCmp;
        }
        return getType().compareTo(other.getType());
    }

    @Override
    public int hashCode() {
        int h = 27;
        h = 31 * h + type.hashCode();
        h = 31 * h + number;
        h = 31 * h + section;
        return h;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Lab)) {
            return false;
        }
        final Lab other = (Lab) o;
        return getNumber() == other.getNumber() && getSection() == other.getSection() && getType().equals(other.getType());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (lecture.length != 1) {
            builder.append(type);
            builder.append(' ');
            builder.append(number);
            builder.append(' ');
        } else {
            final Lecture lecture = this.lecture[0];
            builder.append(lecture);
            builder.append(' ');
        }
        builder.append(isLab ? "LAB" : "TUT");
        builder.append(' ');
        builder.append(section < 10 ? "0" : "");
        builder.append(section);
        return builder.toString();
    }
}
