package ca.ucalgary.cpsc433.constraint.hard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

public class FifthYearConstraint implements HardConstraint {
	private boolean initialized = false;
	private ArrayList<Lecture> fivehundred;
	
	
	@Override
	public boolean isSatisfied(final Schedule schedule) {
		
		if(!initialized){
			initialize(schedule.getEnvironment());
		}
		
		Set<Slot> fiveSlots = new HashSet<Slot>();
		for(final Lecture lecture : fivehundred) {
			final Assign assign = schedule.getAssign(lecture);
			final Slot assigned = assign.getSlot();
			if(assign != null && !fiveSlots.add(assigned)) {
				return false;
			}
		}
		return true;
	}
	
	private void initialize(final Environment environment){
		initialized = true;
		
		final Lecture[] allLectures = environment.getLectures();
		ArrayList<Lecture> fivehundred = new ArrayList<Lecture>();
		
		for (final Lecture lecture : allLectures) {
			if (lecture.getNumber() >= 500 && lecture.getNumber() <600){
				fivehundred.add(lecture);
			}
		}
		
	}

}
