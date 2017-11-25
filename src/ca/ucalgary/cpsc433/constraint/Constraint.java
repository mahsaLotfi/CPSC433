package ca.ucalgary.cpsc433.constraint;

import ca.ucalgary.cpsc433.schedule.Schedule;

/**
 * @author Obicere
 */
public interface Constraint {

    public boolean isSatisfied(final Schedule schedule);

}
