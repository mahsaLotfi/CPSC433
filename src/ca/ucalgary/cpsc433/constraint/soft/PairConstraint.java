package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.Main;
import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Soft Constraint for pair(a,b) where if assign(a) != assign(b)
 * then a penalty will be applied 
 */
public class PairConstraint implements SoftConstraint {

    private final int penalty;

    private final int weight;

    private List<List<Course>> pairCourses;

    private boolean initialized = false;
    
    /**
     * Constructor that initializes the penalty value and its weight
     */
    public PairConstraint() {
        this.penalty = Main.getProperty("pPair", 15);
        this.weight = Main.getProperty("wPair", 4);
    }

    /**
     * Checks if the constraint is satisfied or not, if it is then adds a penalty
     * @param schedule the current schedule to be checked
     * @return penalty value to be applied to the schedule
     */
    public int getPenalty(final Schedule schedule) {
        if (!initialized) {
            initialize(schedule.getEnvironment());
        }
        if (pairCourses == null) {
            return 0;
        }

        int penalty = 0;
        for (final List<Course> pairs : pairCourses) {
            final Iterator<Course> iter = pairs.iterator();
            final Course first = iter.next();
            final Assign assign = schedule.getAssign(first);
            if (assign == null) {
                continue;
            }
            final Slot slot = assign.getSlot();
            while (iter.hasNext()) {
                final Course pair = iter.next();
                final Assign pairAssign = schedule.getAssign(pair);
                if (pairAssign != null) {
                    final Slot s = pairAssign.getSlot();
                    if (!slot.equals(s)) {
                        penalty += this.penalty;
                    }
                }
            }

        }
        return penalty;
    }

    /**
     * Getter for weight variable
     * @return weight weight value of the constraint
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Initializes a list of lists of pairCourses, pair(a,b)
     * @param environment where the values are obtained from
     */
    private void initialize(final Environment environment) {
        initialized = true;

        if (environment.hasPairs()) {
            pairCourses = new LinkedList<>();
            for (final Course course : environment.getCourses()) {
                final Course[] pairs = environment.getPairs(course);
                if (pairs.length == 0) {
                    continue;
                }
                final List<Course> full = new ArrayList<>();
                full.add(course);
                for (final Course subCourse : pairs) {
                    if (subCourse.getID() > course.getID()) {
                        full.add(subCourse);
                    }
                }

                if (full.size() > 1) {
                    pairCourses.add(full);
                }
            }
        }
    }
}
