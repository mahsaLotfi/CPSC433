package ca.ucalgary.cpsc433.schedule;

import ca.ucalgary.cpsc433.constraint.hard.HardConstraint;
import ca.ucalgary.cpsc433.constraint.soft.SoftConstraint;
import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;

import java.util.ArrayList;

/**
 * @author Obicere
 */
public class Schedule implements Comparable<Schedule> {

    private static final Assign[] EMPTY_ASSIGNS = new Assign[0];

    private final Environment environment;

    private final int lectureCount;

    private int evaluation = -1;

    private boolean valid = false;

    private boolean validCached = false;

    private byte[] assigns;

    private byte[] labCounts;

    private byte[] lectureCounts;

    public Schedule(final Environment environment) {
        this(environment, EMPTY_ASSIGNS);
    }

    public Schedule(final Environment environment, final Assign[] assigns) {
        if (environment == null) {
            throw new NullPointerException("environment must be non-null");
        }
        ensureNonNull(assigns);
        this.environment = environment;
        this.lectureCount = environment.getLectureCount();

        buildSlots(assigns);
    }

    private void buildSlots(final Assign[] original) {
        assigns = new byte[original.length];
        labCounts = new byte[environment.getSlotCount()];
        lectureCounts = new byte[environment.getSlotCount()];

        for (final Assign assign : original) {
            final Course course = assign.getCourse();
            final Slot slot = assign.getSlot();
            final int slotID = slot.getSlotID();

            assigns[course.getID()] = (byte) slotID;

            if (course.isLecture()) {
                lectureCounts[slotID]++;
            } else {
                labCounts[slotID]++;
            }
        }
    }

    public Assign[] getAssigns() {
        return getAssigns(0, assigns.length);
    }

    public Assign[] getAssigns(final int start, final int length) {
        final Assign[] newAssigns = new Assign[length];
        for (int i = 0; i < length; i++) {
            newAssigns[i + start] = getAssign(i);
        }
        return newAssigns;
    }

    public Assign getAssign(final Course course) {
        final Slot slot;
        if (course.isLecture()) {
            slot = environment.getSlot(course.getID());
        } else {
            slot = environment.getSlot(course.getID() + lectureCount);
        }
        return Assign.getAssign(course, slot);
    }

    public Assign getAssign(final int courseID) {
        final Course course = getCourse(courseID);
        return getAssign(course);
    }

    private Course getCourse(final int courseID) {
        final Course course;
        if (courseID < lectureCount) {
            course = environment.getLecture(courseID);
        } else {
            course = environment.getLab(courseID - lectureCount);
        }
        return course;
    }

    public int getEvaluation() {
        if (evaluation != -1) {
            return evaluation;
        }
        int evaluation = 0;

        final SoftConstraint[] softConstraints = environment.getSoftConstraints();

        for (final SoftConstraint constraint : softConstraints) {
            final int violations = constraint.getViolations(this);
            evaluation += (violations * constraint.getPenalty() * constraint.getWeight());
        }

        this.evaluation = evaluation;
        return evaluation;
    }

    public boolean isValid() {
        if (validCached) {
            return valid;
        }
        boolean valid = true;

        final HardConstraint[] hardConstraints = environment.getHardConstraints();

        for (final HardConstraint constraint : hardConstraints) {
            final boolean satisfied = constraint.isSatisfied(this);
            if (!satisfied) {
                valid = false;
                break;
            }
        }

        this.valid = valid;
        validCached = true;
        return valid;
    }

    public Environment getEnvironment() {
        return environment;
    }

    private void ensureNonNull(final Assign[] assigns) {
        if (assigns == null) {
            throw new NullPointerException("assigns must be non-null.");
        }
        for (final Assign assign : assigns) {
            if (assign == null) {
                throw new NullPointerException("Elements in assigns must be non-null.");
            }
        }
    }

    public int size() {
        return assigns.length;
    }

    public int getLectureCount(final Slot slot) {
        return lectureCounts[slot.getSlotID()];
    }

    public int getLabCount(final Slot slot) {
        return labCounts[slot.getSlotID()];
    }

    public Course[] getCourses(final Slot slot) {
        final ArrayList<Course> courses = new ArrayList<>();
        for (int i = 0; i < assigns.length; i++) {
            final int assign = assigns[i];
            if (assign == slot.getSlotID()) {
                final Course course = getCourse(i);
                courses.add(course);
            }
        }
        return courses.toArray(new Course[courses.size()]);
    }

    @Override
    public int compareTo(final Schedule o) {
        return Integer.compare(getEvaluation(), o.getEvaluation());
    }
}
