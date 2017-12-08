package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * Hard Constraint for the maximum value that a Slot has for
 * a lab or lecture
 */
public class MaxConstraint implements HardConstraint {

    /**
     * Checks if the current schedule meets the Slot max limit
     * @param schedule current schedule being checked
     * @return true if a lecture or lab doesn't not exceed the slot max value
     */
    public boolean isSatisfied(final Schedule schedule) {
        final Environment environment = schedule.getEnvironment();
        final Slot[] slots = environment.getSlots();
        for (final Slot slot : slots) {
            final int labCount = schedule.getLabCount(slot);
            if (labCount > slot.getLabMax()) {
                return false;
            }
            final int lectureCount = schedule.getLectureCount(slot);
            if (lectureCount > slot.getLectureMax()) {
                return false;
            }
        }
        return true;
    }
}
