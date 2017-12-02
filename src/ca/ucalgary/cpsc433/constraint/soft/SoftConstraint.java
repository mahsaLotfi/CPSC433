package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.schedule.Schedule;

/**
 * @author Obicere
 */
public interface SoftConstraint {

    public int getViolations(final Schedule schedule);

    public int getPenalty();

    public int getWeight();

}
