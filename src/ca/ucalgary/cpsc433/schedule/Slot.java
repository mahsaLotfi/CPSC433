package ca.ucalgary.cpsc433.schedule;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A course is assigned to a Slot, which has a day and time associated with it. It has a min/max number
 * of lectures/labs that can be assigned to it. 
 */
public class Slot implements Comparable<Slot> {

    private static final Map<Integer, Slot> CACHE = new ConcurrentHashMap<>();

    private static int slotCount = 0;

    private int slotID;

    private final Day day;

    private final Time time;

    private int lectureMax = 0;

    private int lectureMin = 0;

    private int labMax = 0;

    private int labMin = 0;

    private boolean lectureInit = false;

    private boolean labInit = false;

    /**
     * Retrieves the slot for a specific day and time, returning the slots contents if a course has
     * been assigned there
     * @param day When the slot is during the week
     * @param time Start time of the slot
     * @return slot of the specified day and time
     */
    public static Slot getSlot(final Day day, final Time time) {
        final int id = getCacheID(day, time);
        final Slot cached = CACHE.get(id);
        if (cached != null) {
            return cached;
        }
        final Slot newSlot = new Slot(day, time);
        CACHE.put(id, newSlot);
        return newSlot;
    }

    /**
     * Checks whether a specified Slot exists
     * @param day When the slot takes place
     * @param time starting time of the slot
     * @return true if the slot exists
     */
    public static boolean exists(final Day day, final Time time) {
        final int id = getCacheID(day, time);
        final Slot cached = CACHE.get(id);
        return cached != null;
    }

    /**
     * @return the slotCount
     */
    public static int getSlotCount() {
        return slotCount;
    }

    /**
     * Retrieves all the slots that have been created
     * @return array of slots
     */
    public static Slot[] getSlots() {
        final Slot[] slots = new Slot[slotCount];
        final Collection<Slot> values = CACHE.values();
        for (final Slot s : values) {
            slots[s.getSlotID()] = s;
        }
        return slots;
    }

    /**
     * Retrieves the cache ID of the specified slot based of its day, hour, and minutes
     * @param day when the slot takes place
     * @param time start time 
     * @return cache ID
     */
    private static int getCacheID(final Day day, final Time time) {
        return day.ordinal() * 2400 + time.getHour() * 60 + time.getMinute();
    }

    /**
     * Constructor that initializes the day and time of a slot, 
     * adding it to the list and increasing the slot count
     * @param day
     * @param time
     */
    private Slot(final Day day, final Time time) {
        this.day = day;
        this.time = time;
        this.slotID = slotCount++;
    }

    /**
     * @return slot ID
     */
    public int getSlotID() {
        return slotID;
    }

    /**
     * @return Day (MONDAY,TUESDAY,FRIDAY)
     */
    public Day getDay() {
        return day;
    }

    /**
     * @return Time of slot
     */
    public Time getTime() {
        return time;
    }

    /**
     * @return end time of the lab
     */
    public Time getLabEndTime() {
        return time.add(0, 50);
    }

    /**
     * @return end time of lecture
     */
    public Time getLectureEndTime() {
        if (day == Day.TUESDAY) {
            if (time.getHour() == 18 && time.getMinute() == 0) {
                return time.add(0, 50);
            } else {
                return time.add(1, 15);
            }
        } else {
            return time.add(0, 50);
        }
    }

    /**
     * @return max lectures for the slot
     */
    public int getLectureMax() {
        return lectureMax;
    }

    /**
     * @return minimum number of lectures for the slot
     */
    public int getLectureMin() {
        return lectureMin;
    }

    /**
     * Sets the number of courses that the slot can have assigned for a maximum and a
     * minimum value
     * @param min minimum number of Lectures for the Slot
     * @param max maximum number of Lectures for the Slot
     */
    public void setLectureLimit(final int min, final int max) {
        if (lectureInit) {
            return;
        }
        this.lectureMax = max;
        this.lectureMin = min;
        this.lectureInit = true;
    }

    /**
     * Sets the minimum and maximum number of labs that can be 
     * scheduled in a slot
     * @param min minimum number of labs
     * @param max maximum number of labs
     */
    public void setLabLimit(final int min, final int max) {
        if (labInit) {
            return;
        }
        this.labMax = max;
        this.labMin = min;
        this.labInit = true;
    }

    /**
     * @return maximum number of labs for the slot
     */
    public int getLabMax() {
        return labMax;
    }

    /**
     * @return minimum number of labs for the slot
     */
    public int getLabMin() {
        return labMin;
    }

    /**
     * @return true the slot is valid for the lecture
     */
    public boolean isValidLectureSlot() {
        return time.isValidLectureTime(day);
    }

    /**
     * @return true if the slot is valid for the lab
     */
    public boolean isValidLabSlot() {
        return time.isValidLabTime(day);
    }

    /**
     * @return The day and time of the slot in string format
     */
    public String toString() {
        return day + ", " + time;
    }

    /**
     * Compares two Slots
     * @param o Slot to be compared to
     * @return an int value depeding if it is > , <, =.
     */
    public int compareTo(final Slot o) {
        final int dayCmp = day.compareTo(o.day);
        if (dayCmp != 0) {
            return dayCmp;
        }
        return getTime().compareTo(o.getTime());
    }
}
