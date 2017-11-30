package ca.ucalgary.cpsc433;

import java.util.PriorityQueue;

import ca.ucalgary.cpsc433.schedule.Assign;

/**
 * 
 * a fixed-size population of assignments
 *
 */
public class Population{
	private int maxSize;
	private int size;
	private Assign leastFit;
	private PriorityQueue<Assign> pq;
	
	public Population(int maxSize) {
		size = 0;
		leastFit = null;
		this.maxSize = maxSize;
		pq = new PriorityQueue<Assign>();
	}
	
	/**
	 * 
	 * @param e assignment to be added
	 * @return true if added successfully
	 */
	public boolean add(Assign e) {
		if (leastFit != null) {
		    if (e.compareTo(leastFit)<0) 
			    leastFit = e;
		} else leastFit = e;
		if (size < maxSize)
			return pq.add(e);
		else
			pq.remove(leastFit);
			return pq.add(e);
	}
	
	/**
	 * 
	 * @return
	 */
	public Assign fittest() {
		return pq.peek();
	}
	
	
}
