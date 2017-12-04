package ca.ucalgary.cpsc433.tree;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.io.InputParser;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.util.Arrays;

/**
 * @author Obicere
 */
public class OrTreeMain {
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
        System.out.println("Starting...");

        boolean solutionFound = true;
        long sum = 0;
        long evaluation = 0;
        int minEval = Integer.MAX_VALUE;
        int maxEval = Integer.MIN_VALUE;
        int length = 10000;
        final Schedule[] schedules = new Schedule[length];

        System.out.println("Generating " + length + " schedules.");

        int lastPercent = -1;
        for (int i = 0; i < length; i++) {
            final OrTree tree = new OrTree(environment);

            final long start = System.nanoTime();
            schedules[i] = tree.search();

            if (schedules[i] == null) {
                System.out.println("No solution for schedule.");
                solutionFound = false;
                break;
            }

            sum += (System.nanoTime() - start);
            final int eval = schedules[i].getEvaluation();

            evaluation += eval;
            if (eval > maxEval) {
                maxEval = eval;
            }
            if (eval < minEval) {
                minEval = eval;
            }
            int nextPercent = (100 * i + 100) / length;
            if(nextPercent != lastPercent) {
                lastPercent = nextPercent;
                System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
                System.out.print('[');
                for(int j = 0; j < 20; j++) {
                    if(j * 5 < lastPercent) {
                        System.out.print(':');
                    }                         else {
                        System.out.print(' ');
                    }
                }
                System.out.print(']');
                System.out.print(' ');
                if(nextPercent < 100) System.out.print(' ');
                if(nextPercent < 10) System.out.print(' ');
                System.out.print(nextPercent);
                System.out.print('%');
            }
        }
        System.out.println();

        System.out.println(Arrays.toString(Schedule.failures));

        if (solutionFound) {
            System.out.printf("Average time per tree: %f\n", (sum / (double) length));
            System.out.printf("Average evaluation: %f, Min: %d, Max: %d\n", (evaluation / (double) length), minEval, maxEval);

            final Schedule schedule = schedules[(int) (Math.random() * schedules.length)];

            System.out.println();
            System.out.println("Evaluation: " + schedule.getEvaluation());
            System.out.println("Schedule for: " + environment.getName());

            final Slot[] slots = environment.getSlots();
            for (final Slot slot : slots) {
                System.out.println(slot + ", " + slot.getLectureMax() + ", " + slot.getLabMax() + ", " + slot.getLectureMin() + ", " + slot.getLabMin());
                final Course[] courses = schedule.getCourses(slot);

                for (final Course course : courses) {
                    System.out.println("\t" + course);
                }
                System.out.println();
            }
        }
    }
}
