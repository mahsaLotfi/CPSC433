package ca.ucalgary.cpsc433.schedule;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Obicere
 */
public class Slot {

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

    public static boolean exists(final Day day, final Time time) {
        final int id = getCacheID(day, time);
        final Slot cached = CACHE.get(id);
        return cached != null;
    }

    public static int getSlotCount() {
        return slotCount;
    }

    public static Slot[] getSlots() {
        final Slot[] slots = new Slot[slotCount];
        final Collection<Slot> values = CACHE.values();
        for (final Slot s : values) {
            slots[s.getSlotID()] = s;
        }
        return slots;
    }

    private static int getCacheID(final Day day, final Time time) {
        return day.ordinal() * 2400 + time.getHour() * 60 + time.getMinute();
    }

    private Slot(final Day day, final Time time) {
        this.day = day;
        this.time = time;
        this.slotID = slotCount++;
    }

    public int getSlotID() {
        return slotID;
    }

    public Day getDay() {
        return day;
    }

    public Time getTime() {
        return time;
    }

    public Time getLabEndTime() {
        return time.add(0, 50);
    }

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

    public int getLectureMax() {
        return lectureMax;
    }

    public int getLectureMin() {
        return lectureMin;
    }

    public void setLectureLimit(final int min, final int max) {
        if (lectureInit) {
            return;
        }
        this.lectureMax = max;
        this.lectureMin = min;
        this.lectureInit = true;
    }

    public void setLabLimit(final int min, final int max) {
        if (labInit) {
            return;
        }
        this.labMax = max;
        this.labMin = min;
        this.labInit = true;
    }

    public int getLabMax() {
        return labMax;
    }

    public int getLabMin() {
        return labMin;
    }

    public boolean isValidLectureSlot() {
        return time.isValidLectureTime(day);
    }

    public boolean isValidLabSlot() {
        return time.isValidLabTime(day);
    }

    @Override
    public String toString() {
        return day + ", " + time;
    }
}
