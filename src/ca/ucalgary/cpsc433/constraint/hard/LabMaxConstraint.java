package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * @author Obicere
 */
public class LabMaxConstraint implements HardConstraint {

    @Override
    public boolean isSatisfied(final Schedule schedule) {
        final Environment environment = schedule.getEnvironment();
        final Slot[] slots = environment.getLabSlots();
        for (final Slot slot : slots) {
            final int count = schedule.getLabCount(slot);
            if (count > slot.getLabMax()) {
                return false;
            }
        }
        return true;
    }
}
