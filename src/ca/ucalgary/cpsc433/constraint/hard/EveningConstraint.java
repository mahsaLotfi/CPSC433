package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;

import java.util.ArrayList;

/**
 * Hard Constraint that all evening classes are in evening slots, represented
 * with a section number greater than or equal to 90
 *
 */
public class EveningConstraint implements HardConstraint {
    private boolean initialized = false;
    private ArrayList<Course> evening;

    /**
     * @param schedule The current schedule to be checked
     * @return true if satisfied if all evening courses are in evening slots
     */
    public boolean isSatisfied(final Schedule schedule) {
        if (!initialized) {
            initialize(schedule.getEnvironment());
        }
        for (final Course course : evening) {
            final Assign assign = schedule.getAssign(course);
            if (assign != null && !assign.getSlot().getTime().isEvening()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Initializes an ArrayList of all the evening courses
     * @param environment used to initialize the list
     */
    private void initialize(final Environment environment) {
        initialized = true;

        final Course[] allCourses = environment.getCourses();
        evening = new ArrayList<>();

        for (final Course course : allCourses) {
            if (course.getSection() >= 90) {
                evening.add(course);
            }
        }
    }

}
