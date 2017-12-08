package ca.ucalgary.cpsc433.set;


import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.util.SortedList;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * a fixed-size population of assignments
 */
public class Population {
    private static final double THRESHOLD = 0.9;
    private static final double P         = 0.1;
    private static final double Q         = 0.9;

    private int                  minSize;
    private int                  maxSize;
    private SortedList<Schedule> sl;
    private Environment          env;
    private Random               rand;
    private int fittest     = 0;
    private int fittestEval = Integer.MAX_VALUE;

    public Population(int minSize, int maxSize, Environment e, final PriorityQueue<Schedule> population) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        rand = new Random();
        env = e;
        sl = new SortedList<>(population);

        final Iterator<Schedule> iterator = sl.iterator();
        fittestEval = iterator.next().getEvaluation();
        fittest = 1;
        while (iterator.hasNext()) {
            int next = iterator.next().getEvaluation();
            if (fittestEval == next) {
                fittest++;
            }  else {
                break;
            }
        }
    }

    /**
     * @param e Schedule to be added
     * @return true if added successfully
     */
    private boolean add(Schedule e) {
        if (e.getEvaluation() == fittestEval) {
            fittest++;
        } else if (e.getEvaluation() < fittestEval) {
            fittest = 1;
            fittestEval = e.getEvaluation();
            return sl.add(e);
        }
        return true;
    }

    public void operate() {
        if (sl.size() < minSize) {
            crossover();
        } else if (sl.size() > maxSize) {
            purge();
        } else {
            final double prob = rand.nextDouble();
            if (prob <= P) {
                crossover();
            } else if (prob <= Q) {
                mutate();
            } else {
                purge();
            }
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
     * new Schedule is valid, it will be added to the population.
     * Otherwise the new child is stillborn and not included in
     * the population
     */
    private void crossover() {
        if (sl.size() < 2) {
            return;
        }
        Schedule parent1 = sl.get(rand.nextInt(fittest));
        Schedule parent2 = sl.get(fittest + rand.nextInt(sl.size() - fittest));  // randomly chosen from the remaining individulas

        // generate the child
        Assign[] chromosome = new Assign[sl.get(0).size()];
        for (int i = 0; i < chromosome.length; ++i) {
            if (rand.nextBoolean()) {
                chromosome[i] = Assign.getAssign(parent1.getAssigns()[i].getCourse(), parent1.getAssigns()[i].getSlot());
            } else {
                chromosome[i] = Assign.getAssign(parent2.getAssigns()[i].getCourse(), parent2.getAssigns()[i].getSlot());
            }
        }


        Schedule child = new Schedule(env, chromosome);

        if (child.isValid()) {
            this.add(child);
        }
    }

    /**
     * Mutate all genes of a random (excluding the fittest(s)) individual
     * create a new Schedule, if the mutant is valid, it will be added to
     * the population
     */
    private void mutate() {
        double r1 = rand.nextDouble();    // probability of mutation taking place
        double r2 = rand.nextDouble();    // random number for slot selection

        if (sl.size() < 2) {
            return;
        }
        if (r1 > THRESHOLD) {
            Schedule base = sl.get(fittest + rand.nextInt(sl.size() - fittestEval));
            Assign[] baseAssigns = base.getAssigns();
            Assign[] chromosome = new Assign[baseAssigns.length];
            for (int i = 0; i < base.size(); ++i) {
                Assign[] a = base.getAssigns();
                a[i] = Assign.getAssign(a[i].getCourse(), env.getSlots()[(int) Math.floor(r2 * env.getSlotCount())]);
                if ((new Schedule(env, a)).isValid()) {
                    chromosome[i] = Assign.getAssign(a[i].getCourse(), a[i].getSlot());
                } else {
                    chromosome[i] = Assign.getAssign(baseAssigns[i].getCourse(), baseAssigns[i].getSlot());
                }
            }
            Schedule child = new Schedule(env, chromosome);
            if (child.isValid()) {
                this.add(child);
            }

        }

    }

    private void purge() {
        Schedule remove = sl.get(fittest + rand.nextInt(sl.size() - fittest));
        sl.remove(remove);
    }

}
