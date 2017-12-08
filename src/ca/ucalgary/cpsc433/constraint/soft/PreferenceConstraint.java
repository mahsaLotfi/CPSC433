package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.Main;
import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Preference;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * Soft Constraint for the preference of a course to be scheduled at a particular time.
 * If the assign does not equal the preferred slot the pref value will be applied as a penalty
 */
public class PreferenceConstraint implements SoftConstraint {

    private final int weight;

    /**
     * Constructor value for initializing the weight of the constraint
     */
    public PreferenceConstraint() {
        this.weight = Main.getProperty("wPref", 5);
    }

    /**
     * If the constraint is violated then the penalty value will be calculated
     * @param schedule current schedule being checked
     * @return penalty int value to be added
     */
    public int getPenalty(final Schedule schedule) {
        final Environment environment = schedule.getEnvironment();
        if (!environment.hasPreferences()) {
            return 0;
        }
        int penalty = 0;

        for (final Course course : environment.getCourses()) {
            final Preference[] preferences = environment.getPreferences(course);
            if (preferences.length == 0) {
                continue;
            }
            final Assign assign = schedule.getAssign(course);
            if (assign == null) {
                continue;
            }
            final Slot slot = assign.getSlot();
            for (final Preference preference : preferences) {
                final Assign prefAssign = preference.getAssign();
                final Slot prefSlot = prefAssign.getSlot();
                if (!slot.equals(prefSlot)) {
                    penalty += preference.getValue();
                }  
                /*else {
                    break;
                }*/
            }
        }
        return penalty;
    }

    /**
     * Getter for weight variable
     * @return weight multiplier value for penalty
     */
    public int getWeight() {
        return weight;
    }
}
