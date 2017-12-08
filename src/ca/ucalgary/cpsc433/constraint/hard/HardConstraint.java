package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.schedule.Schedule;

/**
 * Interface for Hard Constraints, determines validity of Or-Tree
 */
public interface HardConstraint {

	/**
	 * Checks the current schedule to see if it satisfies the hard constraint
	 * @param schedule current schedule being checked
	 * @return true if constraint is satisfied
	 */
    public boolean isSatisfied(final Schedule schedule);

}
