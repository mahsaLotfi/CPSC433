package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class Lecture implements Course {

    private Lab[] labs;

    private final String type;

    private final int number;

    private final int section;

    public Lecture(final String type, final int number, final int section) {
        if (type == null || type.isEmpty()) {
            throw new NullPointerException("type must be non-null and non-empty");
        }
        this.type = type;
        this.number = number;
        this.section = section;
    }

    public void setLabs(final Lab[] labs) {
        if (this.labs != null) {
            throw new AssertionError("labs already initialized");
        }
        if (labs == null) {
            throw new NullPointerException("labs must be non-null");
        }
        this.labs = labs.clone();
    }

    public Lab[] getLabs() {
        if (labs == null) {
            throw new AssertionError("Lecture has not yet been fully initialized.");
        }
        return labs.clone();
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getSection() {
        return section;
    }

    @Override
    public boolean isLecture() {
        return true;
    }

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
        if (!(o instanceof Lecture)) {
            return false;
        }
        final Lecture other = (Lecture) o;
        return getType().equals(other.getType()) && getNumber() == other.getNumber() && getSection() == other.getSection();
    }

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
