package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.io.InputParser;

/**
 * @author Obicere
 */
public class Main {
    public static void main(final String[] args) {
        if(args.length == 0) {
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
            if (lecture.getLabs() != null) {
                for (final Lab lab : lecture.getLabs()) {
                    System.out.print('\t');
                    System.out.println(lab);
                }
            }
            System.out.println();
        }
    }
}
