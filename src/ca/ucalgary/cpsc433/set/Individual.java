package ca.ucalgary.cpsc433.set;

import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;

public class Individual implements Comparable<Individual>{
    private Schedule schedule;
    private int fitness;
    public Individual(Schedule s, int v) {
    	schedule = s;
    	fitness = v;
    }
	@Override
	public int compareTo(Individual o) {
		if (this.fitness < o.fitness) return -1;
		else if (this.fitness > o.fitness) return 0;
		else return 0;
	}
	
	public int size() {
		return schedule.size();
	}
	
	public Assign[] getLeftHaploid(int cutLen) {
		return schedule.getAssigns(0, cutLen);
	}
	
	public Assign[] getRightHaploid(int cutLen) {
		return schedule.getAssigns(cutLen, schedule.size()-cutLen);
	}
	
	public Assign[] getSegment(int start, int length) {
		return schedule.getAssigns(start, length);
	}

}
