package ca.ucalgary.cpsc433.schedule;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Obicere
 */
public class Schedule implements Cloneable, Comparable<Schedule> {

    private static final Assign[] EMPTY_ASSIGNS = new Assign[0];

    private final Assign[] assigns;

    private final Environment environment;

    private int evaluation = -1;

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
        int newEvaluation = 0;

        // TODO compute evaluation of soft constraints

        this.evaluation = newEvaluation;
        return evaluation;
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

    public Course[] getCourses(final Slot slot) {
        final List<Course> courses = new ArrayList<>();
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
