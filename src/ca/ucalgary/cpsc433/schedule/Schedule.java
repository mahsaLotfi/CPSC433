package ca.ucalgary.cpsc433.schedule;

import ca.ucalgary.cpsc433.environment.Environment;

/**
 * @author Obicere
 */
public class Schedule implements Cloneable {

    private static final Assign[] EMPTY_ASSIGNS = new Assign[0];

    private final Assign[] assigns;

    private int evaluation = -1;

    public Schedule() {
        this.assigns = EMPTY_ASSIGNS;
    }

    public Schedule(final Schedule schedule) {
        this.assigns = schedule.assigns.clone();
    }

    public Schedule(final Schedule schedule, final Assign... newAssigns) {
        if (newAssigns == null) {
            throw new NullPointerException("assigns must be non-null.");
        }
        final Assign[] old = schedule.assigns;

        this.assigns = new Assign[old.length + newAssigns.length];

        System.arraycopy(old, 0, assigns, 0, old.length);
        System.arraycopy(newAssigns, 0, assigns, old.length, newAssigns.length);
    }

    public Assign[] getAssigns() {
        // note the clone call here. O(n), don't use excessively.
        return assigns.clone();
    }

    public Schedule insert(final Assign[] assigns) {
        ensureNonNull(assigns);
        return new Schedule(this, assigns);
    }

    public int getEvaluation(final Environment environment) {
        if(evaluation != -1) {
            return evaluation;
        }
        int newEvaluation = 0;

        // TODO compute evaluation of soft constraints

        this.evaluation = newEvaluation;
        return evaluation;
    }

    @Override
    public Schedule clone() {
        try {
            super.clone();
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Schedule(this);
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
}
