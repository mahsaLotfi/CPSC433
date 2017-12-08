package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.schedule.Day;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.schedule.Time;

/**
 * Hard constraint that determines whether a schedule has a lecture on
 * Tuesday from 11 - 12
 */
public class TuesdayElevenConstraint implements HardConstraint {

    private Slot tuesday;
    private boolean initialized = false;

    /**
     * True if no lecture on Tuesday at 11
     * @param schedule the schedule to be checked if the constraint is true
     * @return true if Tuesday is empty
     */
    public boolean isSatisfied(final Schedule schedule) {
        if (!initialized) {
            if (Slot.exists(Day.TUESDAY, new Time(11, 0))) {
                tuesday = Slot.getSlot(Day.TUESDAY, new Time(11, 0));
            }
            initialized = true;
        }
        if (tuesday == null) {
            return true;
        }
        return schedule.getLectureCount(tuesday) == 0;
    }


}
