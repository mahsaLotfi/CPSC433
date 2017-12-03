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
public class Schedule implements Cloneable, Comparable<Schedule> {

    private static final Assign[] EMPTY_ASSIGNS = new Assign[0];

    private final Assign[] assigns;

    private final Environment environment;

    private int evaluation = -1;

    private boolean valid = false;

    private boolean validCached = false;

    private int[] labCounts;

    private int[] lectureCounts;

    public Schedule(final Environment environment) {
        this(environment, EMPTY_ASSIGNS);
    }

    public Schedule(final Environment environment, final Schedule schedule) {
        this(environment, schedule.assigns.clone());
    }

    public Schedule(final Environment environment, final Assign[] assigns) {
        if (environment == null) {
            throw new NullPointerException("environment must be non-null");
        }
        ensureNonNull(assigns);
        this.environment = environment;
        this.assigns = assigns.clone();

        buildCounts();
    }

    public Schedule(final Environment environment, final Schedule schedule, final Assign... newAssigns) {
        if (environment == null) {
            throw new NullPointerException("environment must be non-null");
        }
        if (newAssigns == null) {
            throw new NullPointerException("assigns must be non-null.");
        }
        final Assign[] old = schedule.assigns;

        this.environment = environment;
        this.assigns = new Assign[old.length + newAssigns.length];

        System.arraycopy(old, 0, assigns, 0, old.length);
        System.arraycopy(newAssigns, 0, assigns, old.length, newAssigns.length);

        buildCounts();
    }

    private void buildCounts() {
        labCounts = new int[environment.getSlotCount()];
        lectureCounts = new int[environment.getSlotCount()];

        for (final Assign assign : assigns) {
            final Course course = assign.getCourse();
            if (course.isLecture()) {
                lectureCounts[assign.getSlot().getSlotID()]++;
            } else {
                labCounts[assign.getSlot().getSlotID()]++;
            }
        }
    }

    public Assign[] getAssigns() {
        // note the clone call here. O(n), don't use excessively.
        return assigns.clone();
    }

    public Assign[] getAssigns(final int start, final int length) {
        return Arrays.copyOfRange(assigns, start, length);
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
        for (final Assign assign : assigns) {
            if (assign.getSlot().equals(slot)) {
                courses.add(assign.getCourse());
            }
        }
        return courses.toArray(new Course[courses.size()]);
    }

    @Override
    public int compareTo(final Schedule o) {
        return Integer.compare(getEvaluation(), o.getEvaluation());
    }

    @Override
    public Schedule clone() {
        try {
            super.clone();
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Schedule(environment, this);
    }
}
