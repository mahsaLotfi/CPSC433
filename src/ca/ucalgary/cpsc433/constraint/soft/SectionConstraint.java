package ca.ucalgary.cpsc433.constraint.soft;

import ca.ucalgary.cpsc433.Main;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Schedule;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Soft Constraint that adds a certain penalty value if there is a
 * violation of the constraint. 
 */
public class SectionConstraint implements SoftConstraint {

    private final int penalty;

    private final int weight;

    private boolean initialized = false;

    private Set<Lecture[]> lectureGroups;
    
    /**
     * Constructor that initializes the weight and penalty value for the soft constraint
     */
    public SectionConstraint() {
        this.penalty = Main.getProperty("pSec", 10);
        this.weight = Main.getProperty("wSec", 3);
    }

    /**
     * @param schedule the current schedule that is checked for penalties
     * @return penalty integer value to be added
     */
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

    /**
     * Getter for weight
     * @return weight of penalty
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Initializes a Set of Lectures to be compared to
     * @param environment where the Lectures are obtained from
     */
    private void initialize(final Environment environment) {
        initialized = true;

        final Lecture[] lectures = environment.getLectures();

        this.lectureGroups = new HashSet<>();

        for (final Lecture lecture : lectures) {
            final Lecture[] others = environment.getLectures(lecture.getType(), lecture.getNumber());
            final List<Lecture> filtered = new ArrayList<>();
            for (final Lecture other : others){
            	if (other.getID() >= lecture.getID()){
            		filtered.add(other);
            	}
            }
            final Lecture[] newList = filtered.toArray(new Lecture[filtered.size()]);
            lectureGroups.add(newList);
        }
    }
}
