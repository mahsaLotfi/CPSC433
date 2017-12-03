package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.environment.NotCompatible;
import ca.ucalgary.cpsc433.environment.Pair;
import ca.ucalgary.cpsc433.environment.PartialAssign;
import ca.ucalgary.cpsc433.environment.Preference;
import ca.ucalgary.cpsc433.environment.Unwanted;
import ca.ucalgary.cpsc433.io.InputParser;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.tree.OrTree;

import java.util.Arrays;

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

        for (final Preference preference : environment.getPreferences()) {
            System.out.println(preference);
        }
        System.out.println();

        for (final NotCompatible compatible : environment.getNotCompatibles()) {
            System.out.println(compatible);
        }
        System.out.println();

        for (final Pair pair : environment.getPairs()) {
            System.out.println(pair);
        }
        System.out.println();

        for (final PartialAssign partialAssign : environment.getPartialAssigns()) {
            System.out.println(partialAssign);
        }
        System.out.println();

        for (final Unwanted unwanted : environment.getUnwanted()) {
            System.out.println(unwanted);
        }
        System.out.println();

        long sum = 0;
        int length = 100000;
        final Schedule[] schedules = new Schedule[length];
        for (int i = 0; i < length; i++) {
            final OrTree tree = new OrTree(environment);

            final long start = System.nanoTime();
            schedules[i] = tree.search();
            sum += (System.nanoTime() - start);

        }
        System.out.printf("Average time per tree: %f\n", (sum / (double) length));

        final Schedule schedule = schedules[0];

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

        System.out.println(Arrays.toString(environment.getDirectory().getCourses("CPSC", 413)));
        System.out.println(Arrays.toString(environment.getDirectory().getLectures("CPSC", 413)));
        System.out.println(Arrays.toString(environment.getDirectory().getLabs("CPSC", 413)));
    }
}
