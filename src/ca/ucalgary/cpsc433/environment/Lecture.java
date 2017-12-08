package ca.ucalgary.cpsc433.environment;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  Lecture class will implements the Course interface.
 *  
 * @author Obicere
 */
public class Lecture implements Course {

    private static final Map<String, Lecture> CACHE = new ConcurrentHashMap<>();

    private static int lectureCount = 0;

    private final int lectureID;

    private Lab[] labs;

    private final String type;

    private final int number;

    private final int section;

    
    public static Lecture getLecture(final String type, final int number, final int section) {
        final String id = getCacheID(type, number, section);
        final Lecture cached = CACHE.get(id);
        if (cached != null) {
            return cached;
        }
        final Lecture newLecture = new Lecture(type, number, section);
        CACHE.put(id, newLecture);
        return newLecture;
    }

    public static int getLectureCount() {
        return lectureCount;
    }

    public static Lecture[] getLectures() {
        final Lecture[] lectures = new Lecture[lectureCount];
        final Collection<Lecture> values = CACHE.values();
        for (final Lecture l : values) {
            lectures[l.getID()] = l;
        }
        return lectures;
    }

    /**
     * Returns the id which is an string of type, number and section put together.
     * @param type
     * @param number
     * @param section
     * @return String
     */
    private static String getCacheID(final String type, final int number, final int section) {
        return type + number + section;
    }

    /**
     * This Constructor will initialize type, number & section with use of arguments provided.
     * @param type
     * @param number
     * @param section
     */
    Lecture(final String type, final int number, final int section) {
        if (type == null || type.isEmpty()) {
            throw new NullPointerException("type must be non-null and non-empty");
        }
        this.type = type;
        this.number = number;
        this.section = section;

        this.lectureID = lectureCount++;
    }

    /**
     * Getter method for lecture ID.
     * @return lectureID
     */
    @Override
    public int getID() {
        return lectureID;
    }

    /**
     * Setter method for labs.
     * @return section
     */
    public void setLabs(final Lab[] labs) {
        if (this.labs != null) {
            throw new AssertionError("labs already initialized");
        }
        if (labs == null) {
            throw new NullPointerException("labs must be non-null");
        }
        this.labs = labs.clone();
    }

    /**
     * Getter method for labs. 
     * @return labs
     */
    public Lab[] getLabs() {
        if (labs == null) {
            throw new AssertionError("Lecture has not yet been fully initialized.");
        }
        return labs.clone();
    }

    /**
     * Getter method for number.
     * @return number
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * Getter method for type.
     * @return type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Getter method for section.
     * @return section
     */
    @Override
    public int getSection() {
        return section;
    }

    /**
     * Returns true if the object is a Lecture object. False if it is not.
     * @return boolean
     */
    @Override
    public boolean isLecture() {
        return true;
    }

    
    /**
     * Compares Course object o with a Lecture object. Returns 0 if they are the same, 
     * returns -1 if o is a Lab object.
     * @return int
     */
    @Override
    public int compareTo(final Course o) {
        if (o instanceof Lab) {
            return -1;
        }
        final Lecture other = (Lecture) o;
        final int numberCompare = Integer.compare(getNumber(), other.getNumber());
        if (numberCompare != 0) {
            return numberCompare;
        }
        final int sectionCompare = Integer.compare(getSection(), other.getSection());
        if (sectionCompare != 0) {
            return sectionCompare;
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
        if (!(o instanceof Lecture)) {
            return false;
        }
        final Lecture other = (Lecture) o;
        return getType().equals(other.getType()) && getNumber() == other.getNumber() && getSection() == other.getSection();
    }

    /**
     * Building the string with this structure: type + " " + number + " LEC " + section
     * @return String 
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(type);
        builder.append(' ');
        builder.append(number);
        builder.append(" LEC ");
        builder.append(section < 10 ? "0" : "");
        builder.append(section);
        return builder.toString();
    }
}
