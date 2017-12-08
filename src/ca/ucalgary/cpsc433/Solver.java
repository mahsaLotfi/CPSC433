package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.set.Population;
import ca.ucalgary.cpsc433.tree.OrTree;

import java.util.PriorityQueue;

/**
 * @author Obicere
 */
public class Solver implements Runnable {

    private final Environment environment;

    private volatile Schedule solution = null;

    private final int setinit;

    private final int setmin;

    private final int setmax;

    private final int gens;

    private final int goal;

    public Solver(final Environment environment) {
        if (environment == null) {
            throw new NullPointerException("environment must be non-null");
        }
        this.environment = environment;

        this.setinit = Main.getProperty("setinit", 50000);
        this.setmin = Main.getProperty("setmin", 10000);
        this.setmax = Main.getProperty("setmax", 100000);
        this.gens = Main.getProperty("gens", -1);
        this.goal = Main.getProperty("goal", 0);
    }

    @Override
    public void run() {
        final PriorityQueue<Schedule> schedules = initSchedules();

        if (schedules == null) {
            System.out.println("Instance has no solution.");
            return;
        }

        int generations = 0;
        int bestEval = schedules.peek().getEvaluation();

        // INITIALIZE THE POPULATION HERE
        Population culture = new Population(1000,environment);
        // MAKE USE OF THE SortedList(PriorityQueue) CONSTRUCTOR
        // USE setmin AND setmax FOR THE LIMITS OF THE POPULATION

        while ((gens < 0 || generations < gens) && bestEval > goal) {
            if (Thread.interrupted()) {
                break;
            }

            // ADD YOUR SINGLE OPERATION HERE

            // UPDATE THE BEST EVALUATION HERE
            // bestEval = schedules.peek().getEvaluation();
            generations++;
        }

        solution = schedules.poll();
    }

    public Schedule getSolution() {
        return solution;
    }

    private PriorityQueue<Schedule> initSchedules() {
        final OrTree solver = new OrTree(environment);
        final Schedule first = solver.search();

        if (first == null) {
            return null;
        }
        final PriorityQueue<Schedule> schedules = new PriorityQueue<>(setinit);

        schedules.add(first);

        for (int i = 1; i < setinit; i++) {
            final Schedule next = solver.search();
            if (next == null) {
                throw new AssertionError("Schedule should not be null. Error with tree.");
            }
            schedules.add(next);
        }
        return schedules;
    }
}
