package ca.ucalgary.cpsc433;

import java.util.PriorityQueue;

/**
 * 
 * a fixed-size population of assignments
 *
 */
public class Population{
	private int maxSize;
	private int size;
	private Individual leastFit;
	private PriorityQueue<Individual> pq;
	
	public Population(int maxSize) {
		size = 0;
		leastFit = null;
		this.maxSize = maxSize;
		pq = new PriorityQueue<Individual>();
	}
	
	/**
	 * 
	 * @param e individual to be added
	 * @return true if added successfully
	 */
	public boolean add(Individual e) {
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
	public Individual fittest() {
		return pq.peek();
	}
	
	
}
