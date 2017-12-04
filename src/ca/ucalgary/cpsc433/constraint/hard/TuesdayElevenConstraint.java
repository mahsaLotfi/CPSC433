package ca.ucalgary.cpsc433.constraint.hard;


import ca.ucalgary.cpsc433.schedule.Day;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.schedule.Time;
/**
 * 
 * @author Josh
 *
 */
public class TuesdayElevenConstraint implements HardConstraint {
	private Slot tuesday;
	private boolean initialized = false;
	
	public boolean isSatisfied(final Schedule schedule) {
		if (!initialized){
			tuesday = Slot.getSlot(Day.TUESDAY, new Time(11, 0));
			initialized = true;
		}
		if (schedule.getLectureCount(tuesday) != 0 ) return false;
		return true;
	}


}
