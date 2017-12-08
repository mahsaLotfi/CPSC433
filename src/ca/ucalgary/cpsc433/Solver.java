package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.set.Population;
import ca.ucalgary.cpsc433.tree.OrTree;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * A process used to solve the course scheduling problem described by the
 * environment instance. This will utilize an Or-Tree to create an initial
 * population. A genetic algorithm is then applied on the population, to
 * try and optimize the random results generated from the tree.
 * <p>
 * This can be controlled through a number of generations, a specific goal
 * or through a timeout.
 *
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

    private final long timeout;

    /**
     * Initializes the solver. Makes use of the <code>timeout</code>,
     * <code>setinit</code>, <code>setmin</code>, <code>setmax</code>,
     * <code>gens</code> and <code>goal</code> settings.
     *
     * @param environment The environment describing the problem to solve.
     */
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

        final int timeoutS = Main.getProperty("timeout", 15);
        this.timeout = TimeUnit.MILLISECONDS.convert(timeoutS, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        final PriorityQueue<Schedule> schedules = initSchedules();
        if (schedules == null) {
            System.out.println("Instance has no solution.");
            return;
        }

        int generations = 0;
        int bestEval = schedules.peek().getEvaluation();

        Population culture = new Population(setmin, setmax, environment, schedules);
        while ((gens < 0 || generations < gens) && bestEval > goal) {
            if (System.currentTimeMillis() > start + timeout) {
                break;
            }
            if (Thread.interrupted()) {
                break;
            }

            culture.operate();

            bestEval = culture.getFittest().getEvaluation();
            generations++;
        }

        solution = culture.getFittest();
    }

    /**
     * Retrieves the solution. If no solution is found, this will be
     * <code>null</code>. Otherwise, the best solution is retrieved.
     *
     * @return The best solution, if there is one possible.
     * <code>null</code> otherwise.
     */
    public Schedule getSolution() {
        return solution;
    }

    /**
     * Generates the initial population. Adheres to the timeout requested
     * in the properties. Will generate <code>setinit</code> schedules.
     *
     * @return An order heap of all the schedules. <code>null</code> if no
     * solution is possible.
     */
    private PriorityQueue<Schedule> initSchedules() {
        long start = System.currentTimeMillis();
        final OrTree solver = new OrTree(environment);
        final Schedule first = solver.search();

        if (first == null) {
            return null;
        }
        final PriorityQueue<Schedule> schedules = new PriorityQueue<>(setinit);

        schedules.add(first);

        for (int i = 1; i < setinit; i++) {
            if (System.currentTimeMillis() > start + timeout) {
                break;
            }

            final Schedule next = solver.search();
            if (next == null) {
                throw new AssertionError("Schedule should not be null. Error with tree.");
            }
            schedules.add(next);
        }

        solution = schedules.peek();
        return schedules;
    }
}
