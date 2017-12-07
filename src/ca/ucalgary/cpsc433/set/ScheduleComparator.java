package ca.ucalgary.cpsc433.set;

import java.util.Comparator;

import ca.ucalgary.cpsc433.schedule.Schedule;

public class ScheduleComparator implements Comparator<Schedule>{

	@Override
	public int compare(Schedule arg0, Schedule arg1) {
		return arg0.getEvaluation()-arg1.getEvaluation();
	}

}
