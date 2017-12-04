package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.Main;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Obicere
 */
public class SectionConstraint implements SoftConstraint {

    private final int penalty;

    private final int weight;

    private boolean initialized = false;

    private Set<Lecture[]> lectureGroups;

    public SectionConstraint() {
        this.penalty = Main.getProperty("pSec", 10);
        this.weight = Main.getProperty("wSec", 3);
    }

    @Override
    public int getPenalty(final Schedule schedule) {
        if (!initialized) {
            initialize(schedule.getEnvironment());
        }
        int penalty = 0;

        for (final Lecture[] lectures : lectureGroups) {
            final Assign[] assigns = new Assign[lectures.length];
            for (int i = 0; i < assigns.length; i++) {
                assigns[i] = schedule.getAssign(lectures[i]);
                if (assigns[i] == null) {
                    continue;
                }
                final Slot slot = assigns[i].getSlot();
                for (int j = 0; j < i; j++) {
                    if (assigns[j] != null && slot.equals(assigns[j].getSlot())) {
                        penalty += this.penalty;
                    }
                }
            }
        }
        return penalty;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    private void initialize(final Environment environment) {
        initialized = true;

        final Lecture[] lectures = environment.getLectures();

        this.lectureGroups = new HashSet<>();

        for (final Lecture lecture : lectures) {
            final Lecture[] others = environment.getLectures(lecture.getType(), lecture.getNumber());
            lectureGroups.add(others);
        }
    }
}
