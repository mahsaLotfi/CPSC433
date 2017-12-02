package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.schedule.Schedule;

/**
 * @author Obicere
 */
public interface HardConstraint {

    public boolean isSatisfied(final Schedule schedule);

}
