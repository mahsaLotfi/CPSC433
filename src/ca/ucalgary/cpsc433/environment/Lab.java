package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class Lab implements Course {

    private final Lecture lecture;

    private final boolean isLab;

    private final int section;

    public Lab(final Lecture lecture, final boolean isLab, final int section) {
        if (lecture == null) {
            throw new NullPointerException("lecture must be non-null");
        }
        this.lecture = lecture;
        this.isLab = isLab;
        this.section = section;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public boolean isLab() {
        return isLab;
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
    public boolean isLecture() {
        return false;
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(lecture.toString());
        builder.append(' ');
        builder.append(isLab ? "LAB" : "TUT");
        builder.append(' ');
        builder.append(section < 10 ? "0" : "");
        builder.append(section);
        return builder.toString();
    }
}
