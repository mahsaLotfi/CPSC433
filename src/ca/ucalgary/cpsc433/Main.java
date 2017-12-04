package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.io.InputParser;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.tree.OrTree;

/**
 * @author Obicere
 */
public class Main {
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.err.println("Need to run main with input file as first parameter.");
            return;
        }
        final Environment environment;
        try {
            environment = InputParser.parseEnvironment(args[0]);
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        for (final Lecture lecture : environment.getLectures()) {
            System.out.println(lecture);
            for (final Lab lab : lecture.getLabs()) {
                System.out.print('\t');
                System.out.println(lab);
            }
            System.out.println();
        }

        boolean solutionFound = true;
        long sum = 0;
        int length = 1;
        final Schedule[] schedules = new Schedule[length];
        for (int i = 0; i < length; i++) {
            final OrTree tree = new OrTree(environment);

            final long start = System.nanoTime();
            schedules[i] = tree.search();
            sum += (System.nanoTime() - start);

            if (schedules[i] == null) {
                System.out.println("No solution for schedule.");
                solutionFound = false;
                break;
            }
        }

        if (solutionFound) {
            System.out.printf("Average time per tree: %f\n", (sum / (double) length));

            final Schedule schedule = schedules[(int) (Math.random() * schedules.length)];

            System.out.println("Schedule for: " + environment.getName());

            final Slot[] slots = environment.getSlots();
            for (final Slot slot : slots) {
                System.out.println(slot);
                final Course[] courses = schedule.getCourses(slot);

                for (final Course course : courses) {
                    System.out.println("\t" + course);
                }
                System.out.println();
            }
        }
    }
}
