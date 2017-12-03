package ca.ucalgary.cpsc433.schedule;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Lecture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Obicere
 */
public class Assign {

    private static final Map<String, Assign> CACHE = new ConcurrentHashMap<>();

    private final Course course;

    private final Slot slot;

    private Time endTime;

    public static Assign getAssign(final Course course, final Slot slot) {
        final String id = getCacheID(course, slot);
        final Assign cached = CACHE.get(id);
        if (cached != null) {
            return cached;
        }
        final Assign newAssign = new Assign(course, slot);
        CACHE.put(id, newAssign);
        return newAssign;
    }

    private static String getCacheID(final Course course, final Slot slot) {
        return course.toString() + slot.getSlotID();
    }

    private Assign(final Course course, final Slot slot) {
        if (course == null) {
            throw new NullPointerException("course must be non-null.");
        }
        if (slot == null) {
            throw new NullPointerException("slot must be non-null.");
        }
        this.course = course;
        this.slot = slot;
    }

    public Course getCourse() {
        return course;
    }

    public Slot getSlot() {
        return slot;
    }

    public Time getStartTime() {
        return slot.getTime();
    }

    public Time getEndTime() {
        if (endTime != null) {
            return endTime;
        }
        if (course instanceof Lecture) {
            this.endTime = slot.getLectureEndTime();
        } else {
            this.endTime = slot.getLabEndTime();
        }
        return endTime;
    }

    public boolean contains(final Time time) {
        return time.isInRange(getStartTime(), getEndTime());
    }

    @Override
    public int hashCode() {
        return course.hashCode() * 31 + slot.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Assign)) {
            return false;
        }
        final Assign other = (Assign) o;
        return getCourse().equals(other.getCourse()) && getSlot().equals(other.getSlot());
    }

    @Override
    public String toString() {
        return "Assign[" + course + ", " + slot + "]";
    }

}
