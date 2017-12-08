package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.io.InputParser;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author Obicere
 */
public class Main {
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.err.println("Need to run main with input file as first parameter.");
            return;
        }

        try {
            final FileInputStream stream = new FileInputStream("resources/config.properties");

            final Properties properties = new Properties(System.getProperties());

            properties.load(stream);

            System.setProperties(properties);
        } catch (final IOException e) {
            System.err.println("Failed to load properties file.");
        }

        final Environment environment;
        try {
            environment = InputParser.parseEnvironment(args[0]);
        } catch (final Exception e) {
            System.err.println("Failed to load environment: " + e.getMessage());
            return;
        }

        final int timeout = getProperty("timeout", 15);
        final long timeoutms = TimeUnit.MILLISECONDS.convert(timeout, TimeUnit.MINUTES);

        final Solver solver = new Solver(environment);

        final Thread thread = new Thread(solver);

        final long start = System.currentTimeMillis();
        thread.start();

        try {
            if (timeoutms > 0) {
                Thread.sleep(timeoutms);
                thread.interrupt();
            }
            thread.join();
        } catch (final InterruptedException e) {
            System.err.println("Main thread interrupted. Terminating abruptly.");
            return;
        }

        final long end = System.currentTimeMillis() - start;
        final long minutes = end / 60000;
        final long seconds = (end % 60000) / 1000;
        final long milliseconds = (end % 1000);

        // System.out.printf("Done in %d:%02d:%03d%n%n", minutes, seconds, milliseconds);

        final Schedule solution = solver.getSolution();

        System.out.println("Evaluation: " + solution.getEvaluation());

        final Slot[] slots = environment.getSlots();
        Arrays.sort(slots);

        for (final Slot slot : slots) {
            final Course[] courses = solution.getCourses(slot);

            if (courses.length > 0) {
                System.out.println(slot);
                for (final Course course : courses) {
                    System.out.println("  " + course);
                }
                System.out.println();
            }
        }
    }

    public static int getProperty(final String name, final int defaultValue) {
        final String property = System.getProperty(name);
        if (property != null) {
            try {
                return Integer.parseInt(property);
            } catch (final NumberFormatException e) {
                System.err.println("Property: " + name + " must be an integer.");
            }
        }
        return defaultValue;
    }
}
