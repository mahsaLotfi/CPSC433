package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.constraint.Constraint;
import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * @author Obicere
 */
public class LectureMinConstraint implements Constraint {

    @Override
    public boolean isSatisfied(final Schedule schedule) {
        final Environment environment = schedule.getEnvironment();
        final Slot[] slots = environment.getLabSlots();
        for (final Slot slot : slots) {
            final Course[] courses = schedule.getCourses(slot);
            int count = 0;
            for (final Course course : courses) {
                if (!course.isLecture()) {
                    count++;
                }
            }
            if (count > slot.getMax()) {
                return false;
            }
        }
        return true;
    }
}
