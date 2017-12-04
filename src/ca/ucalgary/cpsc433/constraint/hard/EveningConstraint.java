package ca.ucalgary.cpsc433.constraint.hard;

import java.util.ArrayList;
import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;


public class EveningConstraint implements HardConstraint {
	private boolean initialized = false;
	private ArrayList<Course> evening;
	
	
	public boolean isSatisfied(final Schedule schedule) {
		if (!initialized) {
			initialize(schedule.getEnvironment());
		}
		for (final Course course : evening) {
			final Assign assign = schedule.getAssign(course);
			if(assign != null && !assign.getSlot().getTime().isEvening()) {
				return false;
			}
		}
		return true;
	}
	
	private void initialize(final Environment environment) {
		initialized = true;

	    final Lecture[] allLectures = environment.getLectures();
	    evening = new ArrayList<>();

	    for (final Lecture lecture : allLectures) {
	    	if (lecture.getSection() > 9 ) {
	    		evening.add(lecture);
	        }
	    }
	}

}
