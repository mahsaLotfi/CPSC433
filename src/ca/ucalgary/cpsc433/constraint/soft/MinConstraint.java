package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * @author Obicere
 */
public class MinConstraint extends SoftConstraint {

    private final int penaltyLab;

    private final int penaltyLecture;

    private final int weight;

    public MinConstraint() {
        final int pLab = loadPenalty("pLab");
        final int pLecture = loadPenalty("pLec");
        final int wMin = loadPenalty("wMin");

        if (pLab >= 0) {
            penaltyLab = pLab;
        } else {
            penaltyLab = 1;
        }
        if (pLecture >= 0) {
            penaltyLecture = pLecture;
        } else {
            penaltyLecture = 1;
        }
        if (wMin >= 0) {
            weight = wMin;
        } else {
            weight = 1;
        }
    }

    @Override
    public int getPenalty(final Schedule schedule) {
        int penalty = 0;
        final Environment environment = schedule.getEnvironment();
        final Slot[] slots = environment.getSlots();
        for (final Slot slot : slots) {
            final int labCount = schedule.getLabCount(slot);
            if (labCount < slot.getLabMin()) {
                penalty += penaltyLab;
            }
            final int lectureCount = schedule.getLectureCount(slot);
            if (lectureCount < slot.getLectureMin()) {
                penalty += penaltyLecture;
            }
        }
        return penalty;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
