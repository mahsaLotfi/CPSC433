package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * @author Obicere
 */
public class CourseConstraint implements HardConstraint {

    @Override
    public boolean isSatisfied(final Schedule schedule) {
        final Lecture[] lectures = schedule.getEnvironment().getLectures();

        for (final Lecture lecture : lectures) {
            final Lab[] labs = lecture.getLabs();
            final Assign assign = schedule.getAssign(lecture);
            if (assign == null || labs == null || labs.length == 0) {
                continue;
            }
            final Slot lectureSlot = assign.getSlot();
            for (final Lab lab : labs) {
                final Assign labAssign = schedule.getAssign(lab);
                if (labAssign != null) {
                    if (lectureSlot.equals(labAssign.getSlot())) {
                    // if (assign.collides(labAssign)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
