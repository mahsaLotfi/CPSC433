package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.Main;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * Soft Constraint for the minimum labs and lectures for a particular slot 
 */
public class MinConstraint implements SoftConstraint {

    private final int penaltyLab;

    private final int penaltyLecture;

    private final int weight;

    /**
     * Constructor that initializes the penalty and weight values of the constraint
     */
    public MinConstraint() {
        this.penaltyLab = Main.getProperty("pLab", 20);
        this.penaltyLecture = Main.getProperty("pLec", 20);
        this.weight = Main.getProperty("wMin", 5);
    }

    /**
     * Applies the penalty amount if constraint violated
     * @param schedule current schedule being checked
     * @return penalty value to be applied
     */
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

    /**
     * Getter for weight variable
     * @return weight value to be applied to the penalty 
     */
    public int getWeight() {
        return weight;
    }
}
