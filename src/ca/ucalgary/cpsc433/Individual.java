package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.schedule.Schedule;

public class Individual implements Comparable<Individual>{
	private Schedule schedule;
	private int fitness;
	
	/**
	 * 
	 * @param arg0 complete schedule
	 * @param arg1 the fitness value evaluated
	 */
	public Individual(Schedule arg0, int arg1) {
		schedule = arg0;
		fitness = arg1;
	}

	@Override
	public int compareTo(Individual e) {
		if (this.fitness < e.fitness) return -1;
		else if (this.fitness == e.fitness) return 0;
		else return 1;
	}
	
	/**
	 * 
	 * @return
	 */
	public Schedule getSchedule () {
		return schedule;
	}

}
