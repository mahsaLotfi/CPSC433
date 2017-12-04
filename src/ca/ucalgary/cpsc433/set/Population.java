package ca.ucalgary.cpsc433.set;


import java.util.Random;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.util.SortedList;

/**
 * 
 * a fixed-size population of assignments
 *
 */
public class Population{
	private int maxSize;
	private Individual leastFit;
	private SortedList<Individual> sl;
	private Environment env;
	private int generation;
	Random rand;
	
	public Population(int maxSize, Environment e) {
		rand = new Random();
		env = e;
		this.maxSize = maxSize;
		sl = new SortedList<Individual>();
		generation = 0;
	}
	
	/**
	 * 
	 * @param e individual to be added
	 * @return true if added successfully
	 */
	public boolean add(Individual e) {
		if (sl.size() < maxSize)
			return sl.add(e);
		else
			sl.remove(leastFit);
			return sl.add(e);
	}
	
	/**
	 * 
	 * @return
	 */
	public Individual getFittest() {
		return sl.get(0);
	}
	
	/**
	 * Crossover the fittest individual with another individual
	 * randomly selected from the population at a cross point
	 * randomly chosen, two child individuals will be created,
	 * if the new individual is valid, it will be added to the
	 * population
	 */
	public void crossover() {
		if (sl.size()<2) {
			return;
		}
		Individual fittest = sl.get(0);
		Individual random = sl.get(1+rand.nextInt(sl.size()-1));
		int crossPoint = rand.nextInt(sl.get(0).size());
		
		// generate first child
		Assign[] diploid = new Assign[sl.get(0).size()];
		System.arraycopy(fittest.getLeftHaploid(crossPoint), 0, diploid, 0, crossPoint);
		System.arraycopy(random.getRightHaploid(crossPoint), 0, diploid, crossPoint, sl.get(0).size()-crossPoint);
		Schedule fstChild = new Schedule(env);
		fstChild = new Schedule(env, diploid);
		
		// Add the first child if valid
		if (fstChild.isValid()) {
			this.add(new Individual(fstChild, fstChild.getEvaluation()));
		}
		
		// generate second child
		diploid = new Assign[sl.get(0).size()];
		System.arraycopy(random.getLeftHaploid(crossPoint), 0, diploid, 0, crossPoint);
		System.arraycopy(fittest.getRightHaploid(crossPoint), 0, diploid, crossPoint, sl.get(0).size()-crossPoint);
		Schedule sndChild = new Schedule(env, diploid);
		
		// Add the second child if valid
		if (sndChild.isValid()) {
			this.add(new Individual(sndChild, sndChild.getEvaluation()));
		}
		
		++generation;
	}
	
	/**
	 * Mutate segment of a random (excluding the fittest) individual's 
	 * chromosome to create a new individual, if the mutant is valid, 
	 * it will replace the original individual
	 * @param prob, chance that a mutation will occur in a generation
	 * @param length the length of mutate string
	 */
	public void mutate(int prob, int length) {
		if (prob < 0 || prob > 100) {
			throw new IllegalArgumentException();
		}
		if (sl.size()< 2) {
			return;
		}
		if (prob > rand.nextInt(100)) {
			Individual mutant = sl.get(1+rand.nextInt(sl.size()-1)); // random individual except the fittest
			int mutPoint = rand.nextInt(sl.get(0).size()-length);
			Assign[] head = mutant.getSegment(0, mutPoint-length);
			Assign[] endo = mutant.getSegment(mutPoint, length);     // segment to be mutated
			Assign[] tail = mutant.getSegment(mutPoint+length, sl.get(0).size()-(mutPoint+length));
			Assign[] newchromosome = new Assign[sl.get(0).size()];
			
			Slot[] slotList = env.getLectureSlots();
            
			for (int i=0; i<length;++i) {
				endo[i] = Assign.getAssign(endo[i].getCourse(), slotList[rand.nextInt(slotList.length)]);
			}
			
			System.arraycopy(head, 0, newchromosome, 0, mutPoint-length);
			System.arraycopy(endo, 0, newchromosome, mutPoint, length);
			System.arraycopy(tail, 0, newchromosome, mutPoint+length,sl.get(0).size()-(mutPoint+length));
			
			// swap the old individual with the mutated individual
			sl.remove(mutant); 
			Schedule  s = new Schedule(env, newchromosome);
			if (s.isValid()) {
				this.add(new Individual(s, s.getEvaluation()));
			}
		}
		
	}
	
	public int getGeneration() {
		return generation;
	}
	
	
}
