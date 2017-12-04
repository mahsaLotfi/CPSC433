package ca.ucalgary.cpsc433.set;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.io.InputParser;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.tree.OrTree;

public class SetMain {
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

        int length = 10;
        final Schedule[] schedules = new Schedule[length];
        for (int i = 0; i < length; i++) {
            final OrTree tree = new OrTree(environment);

            schedules[i] = tree.search();

            if (schedules[i] == null) {
                System.out.println("No solution for schedule.");
                solutionFound = false;
                break;
            }
        }

        if (solutionFound) {
            Population culture = new Population(10, environment);
            for (Schedule s : schedules) {
                culture.add(new Individual(s, s.getEvaluation()));
            }

            System.out.println("Final fitness:" + culture.getFittest().getFitness());
            Schedule schedule = culture.getFittest().getSchedule();
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
