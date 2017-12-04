package ca.ucalgary.cpsc433.constraint.hard;

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
 * @author Obicere
 */
public class NotCompatibleConstraint implements HardConstraint {

    private List<List<Course>> nonCourses;

    private boolean initialized = false;

    @Override
    public boolean isSatisfied(final Schedule schedule) {
        if (!initialized) {
            initialize(schedule.getEnvironment());
        }
        if (nonCourses == null) {
            return true;
        }

        for (final List<Course> nonCourse : nonCourses) {
            final Iterator<Course> iter = nonCourse.iterator();
            final Course first = iter.next();
            final Assign assign = schedule.getAssign(first);
            if (assign == null) {
                continue;
            }
            final Slot slot = assign.getSlot();
            while (iter.hasNext()) {
                final Course pair = iter.next();
                final Assign nonAssign = schedule.getAssign(pair);
                if (nonAssign != null) {
                    final Slot s = nonAssign.getSlot();
                    if (slot.equals(s)) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    private void initialize(final Environment environment) {
        initialized = true;

        if (environment.hasNonCompatibles()) {
            nonCourses = new LinkedList<>();
            for (final Course course : environment.getCourses()) {
                final Course[] nons = environment.getNonCompatibles(course);
                if (nons.length == 0) {
                    continue;
                }
                final List<Course> full = new ArrayList<>();
                full.add(course);
                for (final Course subCourse : nons) {
                    if (subCourse.getID() > course.getID()) {
                        full.add(subCourse);
                    }
                }

                if (full.size() > 1) {
                    nonCourses.add(full);
                }
            }
        }
    }
}
