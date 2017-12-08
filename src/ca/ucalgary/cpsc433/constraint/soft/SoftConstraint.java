package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.schedule.Schedule;

/**
 * Interface for Soft Constraints
 */
public interface SoftConstraint {
	
	/**
	 * Checks to see if the schedule violates the soft constraint and gets
	 * the penalty value
	 * @param schedule the current schedule to be checked
	 * @return int value of penalty to be added
	 */
    public int getPenalty(final Schedule schedule);

    /**
     * Getter for weight value of constraint
     * @return int weight value
     */
    public int getWeight();
}
