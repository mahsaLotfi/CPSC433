package ca.ucalgary.cpsc433;


import java.util.Random;
import ca.ucalgary.cpsc433.constraint.HardConstraint;
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
	private HardConstraint hc;
	private int generation;
	Random rand;
	
	public Population(int maxSize, Environment e) {
		rand = new Random();
		env = e;
		this.maxSize = maxSize;
		hc = new HardConstraint();
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
		Schedule fstChild = new Schedule();
		fstChild = new Schedule(fstChild, diploid);
		if (hc.isSatisfied(fstChild)) {
			this.add(new Individual(fstChild, fstChild.getEvaluation(env)));
		}
		
		// generate second child
		diploid = new Assign[sl.get(0).size()];
		System.arraycopy(random.getLeftHaploid(crossPoint), 0, diploid, 0, crossPoint);
		System.arraycopy(fittest.getRightHaploid(crossPoint), 0, diploid, crossPoint, sl.get(0).size()-crossPoint);
		Schedule sndChild = new Schedule();
		sndChild = new Schedule(sndChild, diploid);
		if (hc.isSatisfied(sndChild)) {
			this.add(new Individual(sndChild, sndChild.getEvaluation(env)));
		}
		
		++generation;
	}
	
	/**
	 * 
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
			Assign[] endo = mutant.getSegment(mutPoint, length);
			Assign[] tail = mutant.getSegment(mutPoint+length, sl.get(0).size()-(mutPoint+length));
			Assign[] newChromesome = new Assign[sl.get(0).size()];
			
			Slot[] slotList = env.getLectureSlots();
            
			for (int i=0; i<length;++i) {
				endo[i] = new Assign(endo[i].getCourse(),slotList[rand.nextInt(slotList.length)]);
			}
			
			System.arraycopy(head, 0, newChromesome, 0, mutPoint-length);
			System.arraycopy(endo, 0, newChromesome, mutPoint, length);
			System.arraycopy(tail, 0, newChromesome, mutPoint+length,sl.get(0).size()-(mutPoint+length));
			
			// swap the old individual with the mutated individual
			sl.remove(mutant); 
			Schedule  s = new Schedule();
			s = new Schedule(s, newChromesome);
			if (hc.isSatisfied(s)) {
				this.add(new Individual(s, s.getEvaluation(env)));
			}
		}
		
	}
	
	public int getGeneration() {
		return generation;
	}
	
	
}
