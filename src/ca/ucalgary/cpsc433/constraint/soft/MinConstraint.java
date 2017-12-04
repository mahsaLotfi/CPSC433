package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.Main;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * @author Obicere
 */
public class MinConstraint implements SoftConstraint {

    private final int penaltyLab;

    private final int penaltyLecture;

    private final int weight;

    public MinConstraint() {
        this.penaltyLab = Main.getProperty("pLab", 20);
        this.penaltyLecture = Main.getProperty("pLec", 20);
        this.weight = Main.getProperty("wMin", 5);
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
