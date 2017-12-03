package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * @author Obicere
 */
public class LectureMaxConstraint implements HardConstraint {

    @Override
    public boolean isSatisfied(final Schedule schedule) {
        final Environment environment = schedule.getEnvironment();
        final Slot[] slots = environment.getLectureSlots();
        for (final Slot slot : slots) {
            final int count = schedule.getLectureCount(slot);
            if (count > slot.getLectureMax()) {
                return false;
            }
        }
        return true;
    }
}
