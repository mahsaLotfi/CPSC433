package ca.ucalgary.cpsc433;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.io.InputParser;
import ca.ucalgary.cpsc433.util.SortedList;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

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

        for (int i = 1; i <= 5; i++) {
            final long seed = System.currentTimeMillis();
            final Random r1 = new Random(seed);
            final Random r2 = new Random(seed);

            int size = 500000 + 50000 * i;

            System.out.println("Size: " + i);

            long start = System.currentTimeMillis();

            final PriorityQueue<Integer> a = new PriorityQueue<>(size);
            for (int j = 0; j < size; j++) {
                a.add(r1.nextInt());
            }

            System.out.println("\tPQ Populate: " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();

            final SortedList<Integer> b = new SortedList<>(new PriorityQueue<>(a));
            //for (int j = 0; j < size; j++) {
            //    b.add(r2.nextInt());
            //}

            System.out.println("\tSL Populate: " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();

            int c0 = 0;
            for (int j = 0; j < size; j++) {
                int k = r1.nextInt(a.size());
                final Iterator<Integer> iter = a.iterator();
                int t = 0;
                while (k >= 0) {
                    t = iter.next();
                    k--;
                }
                c0 += t;
            }

            System.out.println("\tPQ random sum: " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();

            int c1 = 0;
            for (int j = 0; j < size; j++) {
                c1 += b.get(r2.nextInt(b.size()));
            }

            System.out.println("\tSL random sum: " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();

            while (!a.isEmpty()) {
                int j = r1.nextInt(a.size());
                final Iterator<Integer> iter = a.iterator();
                boolean d = false;
                while (j > 0) {
                    iter.next();
                    j--;
                    d = true;
                }
                if (d) {
                    iter.remove();
                } else {
                    break;
                }
            }

            System.out.println("\tPQ random removal: " + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();

            while (!b.isEmpty()) {
                b.remove(r2.nextInt(b.size()));
            }

            System.out.println("\tSL random removal: " + (System.currentTimeMillis() - start));

            System.out.println();
        }

    }
}
