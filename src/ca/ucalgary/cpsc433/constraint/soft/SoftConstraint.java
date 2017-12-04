package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.schedule.Schedule;

/**
 * @author Obicere
 */
public abstract class SoftConstraint {

    public abstract int getPenalty(final Schedule schedule);

    public abstract int getWeight();

    protected int loadPenalty(final String property) {
        final String labPenalty = System.getProperty(property);
        if (labPenalty != null) {
            try {
                return Integer.parseInt(labPenalty);
            } catch (final NumberFormatException e) {
                System.err.println(property + " must be an integer");
            }
        }
        return -1;
    }
}
