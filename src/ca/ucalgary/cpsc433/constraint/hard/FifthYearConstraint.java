package ca.ucalgary.cpsc433.constraint.hard;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.util.ArrayList;
import java.util.List;

public class FifthYearConstraint implements HardConstraint {
    private boolean initialized = false;
    private Lecture[] fivehundred;


    @Override
    public boolean isSatisfied(final Schedule schedule) {

        if (!initialized) {
            initialize(schedule.getEnvironment());
        }

        final Assign[] assigns = new Assign[fivehundred.length];
        for (int i = 0; i < assigns.length; i++) {
            assigns[i] = schedule.getAssign(fivehundred[i]);
            if (assigns[i] == null) {
                continue;
            }
            final Slot slot = assigns[i].getSlot();
            for (int j = 0; j < i; j++) {
                if (assigns[j] != null && slot.equals(assigns[j].getSlot())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void initialize(final Environment environment) {
        initialized = true;

        final Lecture[] allLectures = environment.getLectures();
        final List<Lecture> fiveLectures = new ArrayList<>();

        for (final Lecture lecture : allLectures) {
            if (lecture.getNumber() >= 500 && lecture.getNumber() < 600) {
                fiveLectures.add(lecture);
            }
        }

        this.fivehundred = fiveLectures.toArray(new Lecture[fiveLectures.size()]);

    }

}
