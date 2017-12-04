package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.Main;
import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Preference;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

/**
 * @author Obicere
 */
public class PreferenceConstraint implements SoftConstraint {

    private final int weight;

    public PreferenceConstraint() {
        this.weight = Main.getProperty("wPref", 5);
    }

    @Override
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

    @Override
    public int getWeight() {
        return weight;
    }
}
