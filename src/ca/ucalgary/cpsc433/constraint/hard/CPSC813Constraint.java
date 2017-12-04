package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Day;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.schedule.Time;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Obicere
 */
public class CPSC813Constraint implements HardConstraint {

    private boolean initialized = false;

    private Slot target;

    private Lecture[] cpsc813;

    private Course[] nonCompatibles;

    @Override
    public boolean isSatisfied(final Schedule schedule) {
        if (!initialized) {
            initialize(schedule.getEnvironment());
        }
        if (!canBeEnforced()) {
            return true;
        }

        final Assign[] assigns = new Assign[nonCompatibles.length];
        for(int i = 0; i < assigns.length; i++){
            assigns[i] = schedule.getAssign(nonCompatibles[i]);
        }
        for (final Lecture lecture : cpsc813) {
            final Assign assign = schedule.getAssign(lecture);
            for (int i = 0; i < nonCompatibles.length; i++) {
                final Course other = nonCompatibles[i];
                if(other != null && assigns[i].collides(assign)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void initialize(final Environment environment) {
        initialized = true;

        final Time time = new Time(18, 0);
        if (!Slot.exists(Day.TUESDAY, time)) {
            return;
        }
        target = Slot.getSlot(Day.TUESDAY, new Time(18, 0));

        final Lecture[] lectures = environment.getLectures("CPSC", 813);
        if (lectures.length == 0) {
            return;
        }
        this.cpsc813 = lectures;

        final List<Course> courses = new LinkedList<>();

        final Course[] cpsc313 = environment.getCourses("CPSC", 313);
        Collections.addAll(courses, cpsc313);

        for (final Course lecture : cpsc313) {
            final Course[] nonCompatible = environment.getNonCompatibles(lecture);
            Collections.addAll(courses, nonCompatible);
        }
        if (courses.isEmpty()) {
            return;
        }

        nonCompatibles = courses.toArray(new Course[courses.size()]);
    }

    private boolean canBeEnforced() {
        return target != null && cpsc813 != null && nonCompatibles != null;
    }
}
