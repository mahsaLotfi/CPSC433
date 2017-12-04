package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.PartialAssign;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Obicere
 */
public class PartialAssignmentConstraint implements HardConstraint {

    private boolean initialized = false;

    private List<PartialAssign> partialCourses;

    @Override
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
            if (!assign.equals(actual)) {
                return false;
            }
        }
        return true;
    }

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
