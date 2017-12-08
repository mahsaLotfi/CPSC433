package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Unwanted;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * Hard Constraint that checks the list of unwanted(a,s) statements, where assign(a) can
 * not be equal to Slot s.
 */
public class UnwantedConstraint implements HardConstraint {
    
	/**
	 * Checks the current schedule and compares to the list of unwanted statements
	 * @param schedule the current schedule to be checked
	 * @return true if assign(a) != s
	 */
    public boolean isSatisfied(final Schedule schedule) {
        final Environment environment = schedule.getEnvironment();
        if (!environment.hasUnwanted()) {
            return true;
        }
        final Course[] courses = environment.getCourses();
        for (final Course course : courses) {
            final Assign assign = schedule.getAssign(course);
            if (assign == null) {
                continue;
            }
            final Slot slot = assign.getSlot();
            final Unwanted[] unwanted = environment.getUnwanted(course);
            for (final Unwanted un : unwanted) {
                if (slot.equals(un.getAssign().getSlot())) {
                    return false;
                }
            }
        }
        return true;
    }
}
