package ca.ucalgary.cpsc433.schedule;

import ca.ucalgary.cpsc433.constraint.hard.HardConstraint;
import ca.ucalgary.cpsc433.constraint.soft.SoftConstraint;
import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Obicere
 */
public class Schedule implements Comparable<Schedule> {

    private static final Assign[] EMPTY_ASSIGNS = new Assign[0];

    private final Environment environment;

    private int evaluation = -1;

    private boolean valid = false;

    private boolean validCached = false;

    private byte[] assigns;

    private byte[] labCounts;

    private byte[] lectureCounts;

    private final int actualAssignments;

    public Schedule(final Environment environment) {
        this(environment, EMPTY_ASSIGNS);
    }

    public Schedule(final Environment environment, final Assign[] assigns) {
        if (environment == null) {
            throw new NullPointerException("environment must be non-null");
        }
        ensureNonNull(assigns);
        this.environment = environment;
        this.actualAssignments = assigns.length;

        buildSlots(assigns);
    }

    private void buildSlots(final Assign[] original) {
        assigns = new byte[environment.getCourseCount()];
        labCounts = new byte[environment.getSlotCount()];
        lectureCounts = new byte[environment.getSlotCount()];

        Arrays.fill(assigns, (byte) -1);

        for (final Assign assign : original) {
            final Course course = assign.getCourse();
            final Slot slot = assign.getSlot();
            final int slotID = slot.getSlotID();
            final int courseID = environment.getCourseID(course);

            assigns[courseID] = (byte) slotID;

            if (course.isLecture()) {
                lectureCounts[slotID]++;
            } else {
                labCounts[slotID]++;
            }
        }
    }

    public Assign[] getAssigns() {
        return getAssigns(0, actualAssignments);
    }

    public Assign[] getAssigns(final int start, final int length) {
        int j = 0;
        final Assign[] newAssigns = new Assign[length];
        for (int i = 0; i < length; i++) {
            final Assign assign = getAssign(i + start);
            if (assign != null) {
                newAssigns[j++] = assign;
            }
        }
        return newAssigns;
    }

    public Assign getAssign(final Course course) {
        final int courseID = environment.getCourseID(course);
        final int slotID = assigns[courseID];
        if (slotID == -1) {
            return null;
        }
        final Slot slot = environment.getSlot(slotID);
        return Assign.getAssign(course, slot);
    }

    public Assign getAssign(final int courseID) {
        final Course course = environment.getCourse(courseID);
        return getAssign(course);
    }

    public int getEvaluation() {
        if (evaluation != -1) {
            return evaluation;
        }
        int evaluation = 0;

        final SoftConstraint[] softConstraints = environment.getSoftConstraints();

        for (final SoftConstraint constraint : softConstraints) {
            final int penalty = constraint.getPenalty(this);
            evaluation += (penalty * constraint.getWeight());
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
                final Course course = environment.getCourse(i);
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
