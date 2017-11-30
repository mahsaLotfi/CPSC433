package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class Lab implements Course {

    private final Lecture lecture;

    private final int section;

    public Lab(final Lecture lecture, final int section) {
        if(lecture == null) {
            throw new NullPointerException("lecture must be non-null");
        }
        this.lecture = lecture;
        this.section = section;
    }

    public Lecture getLecture() {
        return lecture;
    }

    @Override
    public int getNumber() {
        return lecture.getNumber();
    }

    @Override
    public String getType() {
        return lecture.getType();
    }

    @Override
    public int getSection() {
        return section;
    }

    @Override
    public int compareTo(final Course o) {
        if (o instanceof Lecture) {
            return 1;
        }
        final Lab other = (Lab) o;
        final int lectureCompare = getLecture().compareTo(other.getLecture());
        if (lectureCompare != 0) {
            return lectureCompare;
        }
        return Integer.compare(getSection(), o.getSection());
    }

    @Override
    public int hashCode() {
        int h = 27;
        h = 31 * h + lecture.hashCode();
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
        return getLecture().equals(other.getLecture()) && getSection() == other.getSection();
    }
}
