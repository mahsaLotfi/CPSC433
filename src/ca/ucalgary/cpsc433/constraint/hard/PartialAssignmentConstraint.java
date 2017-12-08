package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.PartialAssign;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;

import java.util.LinkedList;
import java.util.List;

/**
 * Hard Constraint that compares the partial assignment condition,
 * assign(a) = partassign(a) 
 */
public class PartialAssignmentConstraint implements HardConstraint {

    private boolean initialized = false;

    private List<PartialAssign> partialCourses;

    /**
     * Checks whether the hard constraint is satisfied or not
     * @param schedule current schedule to be checked
     * @return true if partassign(a) = assign(a)
     */
    public boolean isSatisfied(final Schedule schedule) {
        if (!initialized) {
            initialize(schedule.getEnvironment());
        }
        if (partialCourses == null) {
            return true;
        }
        for (final PartialAssign partialAssign : partialCourses) {
            final Assign assign = partialAssign.getAssign();
            final Assign actual = schedule.getAssign(assign.getCourse());
            if (actual != null && !assign.equals(actual)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Initializes a list of partialCourses from the environment
     * @param environment partialCourses extracted from here
     */
    private void initialize(final Environment environment) {
        initialized = true;

        if (environment.hasPartialAssigns()) {
            this.partialCourses = new LinkedList<>();
            for (final Course course : environment.getCourses()) {
                final PartialAssign assign = environment.getPartialAssign(course);
                if (assign != null) {
                    partialCourses.add(assign);
                }
            }
        }
    }

}
