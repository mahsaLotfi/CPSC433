package ca.ucalgary.cpsc433.set;


import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.util.SortedList;

import java.util.Iterator;
import java.util.Random;

/**
 * a fixed-size population of assignments
 */
public class Population {
    private int                    maxSize;
    private Schedule               leastFit;
    private SortedList<Schedule>   sl;
    private Environment            env;
    private int                    generation;
    Random rand;

    public Population(int maxSize, Environment e) {
    	this.maxSize = maxSize;
        rand = new Random();
        env = e;
        sl = new SortedList<Schedule>(maxSize, new ScheduleComparator());
        generation = 0;
    }

    /**
     * @param e Schedule to be added
     * @return true if added successfully
     */
    public boolean add(Schedule e) {
    	if (sl.isEmpty()) {        // Init leastFit upon first add
    		leastFit = e;
    		return sl.add(e);
    	}
        if (sl.size() < maxSize) {
            if (e.getEvaluation()>leastFit.getEvaluation()) {
            	leastFit = e;
            }
            return sl.add(e);
        } else {
        	if (e.getEvaluation()<leastFit.getEvaluation()) {
              sl.remove(leastFit);
              return sl.add(e);
        	}
        	return false;
        }
    }

    /**
     * @return
     */
    public Schedule getFittest() {
        return sl.get(0);
    }

    /**
     * Crossover the fittest Schedule with another Schedule
     * randomly selected from the population that is not the
     * the fittest, one child Schedules will be created from 
     * genes randomly inherited from the two parents, if the
     *  new Schedule is valid, it will be added to the population.
     *  Otherwise the new child is stillborn and not included in
     *  the population
     */
    public void crossover() {
        if (sl.size() < 2) {
            return;
        }
        Schedule[] fittest = new Schedule[maxSize];
        int best = sl.get(0).getEvaluation(); 
        Iterator<Schedule> it = sl.iterator();
        int idx = 0;
        while (it.hasNext()) {
        	Schedule s = it.next();
        	if (s.getEvaluation() == best) {
        		fittest[idx] = s;
                ++idx;
        	}
        	else break;
        }
        Schedule parent1;
        if (idx > 1) {
        	parent1 = fittest[rand.nextInt(idx-1)];
        } else parent1 = fittest[0];
        Schedule parent2 = sl.get(idx+rand.nextInt(sl.size()-idx));  // randomly chosen from the remaining individulas
        //TODO change the uniform random selection to weighted random selection
        double[] selectSeq = new double[sl.get(0).size()];
        for (int i=0; i<selectSeq.length;++i) {
        	selectSeq[i] = rand.nextGaussian();
        }
        
        // generate the child
        Assign[] chromosome = new Assign[sl.get(0).size()];
        for (int i=0;i<chromosome.length;++i) {
        	if (selectSeq[i] < 0) {
        		chromosome[i] = Assign.getAssign(parent1.getAssigns()[i].getCourse(),parent1.getAssigns()[i].getSlot()); 
        	}
        	else
        		chromosome[i] = Assign.getAssign(parent2.getAssigns()[i].getCourse(),parent2.getAssigns()[i].getSlot()); 
        }

  
        Schedule child = new Schedule(env, chromosome);

        if (child.isValid()) {
            this.add(child);
        }

        ++generation;
    }

    /**
     * Mutate all genes of a random (excluding the fittest(s)) individual
     * create a new Schedule, if the mutant is valid, it will be added to the population
     *
     * @param threshold, controls the chance a mutation will take place
     */
    public void mutate(double threshold) {
    	double r1 = rand.nextDouble();    // probability of mutation taking place
    	double r2 = rand.nextDouble();    // random number for slot selection
    	
    	if (sl.size() < 2) {
    		return;
    	}
    	if (r1 > threshold) {
    		Schedule[] fittest = new Schedule[maxSize];
            int best = sl.get(0).getEvaluation(); 
            Iterator<Schedule> it = sl.iterator();
            int idx = 0;
            while (it.hasNext()) {
            	Schedule s = it.next();
            	if (s.getEvaluation() == best) {
            		fittest[idx] = s;
                    ++idx;
            	}
            	else break;
            }
            
            Schedule base = sl.get(idx+rand.nextInt(sl.size()-idx));
            Assign[] baseAssigns = base.getAssigns();
            Assign[] chromosome = new Assign[sl.get(0).size()];
            for (int i=0;i<base.size();++i) {
            	Assign[] a = base.getAssigns();
            	a[i] = Assign.getAssign(a[i].getCourse(), 
            			env.getSlots()[(int) Math.floor(r2*env.getSlotCount())]);
            	if ((new Schedule(env, a)).isValid()) {
            		chromosome[i] = Assign.getAssign(a[i].getCourse(),a[i].getSlot());
            	} else {
            		chromosome[i] = Assign.getAssign(baseAssigns[i].getCourse(),baseAssigns[i].getSlot());
            	}
            }
            Schedule child = new Schedule(env, chromosome);
            if(child.isValid()) {
            	this.add(child);	
            }
         
        }

    }

    public int getGeneration() {
        return generation;
    }


}
