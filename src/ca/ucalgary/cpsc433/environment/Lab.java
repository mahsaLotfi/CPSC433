package ca.ucalgary.cpsc433.environment;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lab class will implements the Course interface.
 * 
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

    /**
     * Returns the id which is an string of type, number and section put together.
     * @param type
     * @param number
     * @param section
     * @return
     */
    private static String getCacheID(final String type, final int number, final int section) {
        return type + number + section;
    }

    /**
     * This Constructor will initialize type, number & section with use of arguments provided.
     * @param lecture
     * @param isLab
     * @param section
     */
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

    /**
     * Getter method for lab ID.
     * @return labID
     */
    @Override
    public int getID() {
        return labID;
    }

    /**
     * Getter method for lectures.
     * @return lecture[]
     */
    public Lecture[] getLectures() {
        return lecture;
    }

    /**
     * Returns true if the object is a Lab.
     * @return
     */
    public boolean isLab() {
        return isLab;
    }

    /**
     * Getter for number
     */
    @Override
    public int getNumber() {
        return lecture[0].getNumber();
    }

    /**
     * Getter for type
     */
    @Override
    public String getType() {
        return lecture[0].getType();
    }

    /**
     * Getter for section
     */
    @Override
    public int getSection() {
        return section;
    }

    /**
     * Returns false if the object is Lecture.
     */
    @Override
    public boolean isLecture() {
        return false;
    }

    /**
     * Compares Course object o with a Lab object. Returns 0 if they are the same, 
     * returns 1 if o is a Lecture object.
     * @return int
     */
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

    /**
     * Returns hash code value.
     * @return int
     */
    @Override
    public int hashCode() {
        int h = 27;
        h = 31 * h + type.hashCode();
        h = 31 * h + number;
        h = 31 * h + section;
        return h;
    }

    /**
     * Returns true if the argument's type, number & section is 
     * equal to the current object. False if not.
     * @return boolean
     */
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

    /**
     * Builds a string.
     * @return String
     */
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
