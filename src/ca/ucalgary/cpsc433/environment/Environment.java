package ca.ucalgary.cpsc433.environment;

import ca.ucalgary.cpsc433.constraint.hard.HardConstraint;
import ca.ucalgary.cpsc433.constraint.hard.LabMaxConstraint;
import ca.ucalgary.cpsc433.constraint.hard.LectureMaxConstraint;
import ca.ucalgary.cpsc433.constraint.soft.SoftConstraint;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Obicere
 */
public class Environment {

    private static final HardConstraint[] HARD_CONSTRAINTS;

    private static final SoftConstraint[] SOFT_CONSTRAINTS;

    private final String name;

    private final Slot[] labSlots;

    private final Slot[] lectureSlots;

    private final Slot[] slots;

    private final Lab[] labs;

    private final Lecture[] lectures;

    private final NotCompatible[] notCompatibles;

    private final Pair[] pairs;

    private final PartialAssign[] partialAssigns;

    private final Preference[] preferences;

    private final Unwanted[] unwanted;

    private final HardConstraint[] hardConstraints = HARD_CONSTRAINTS;

    private final SoftConstraint[] softConstraints = SOFT_CONSTRAINTS;

    private final CourseDirectory directory;

    static {
        final List<HardConstraint> hard = new ArrayList<>();
        hard.add(new LectureMaxConstraint());
        hard.add(new LabMaxConstraint());

        final List<SoftConstraint> soft = new ArrayList<>();

        HARD_CONSTRAINTS = hard.toArray(new HardConstraint[hard.size()]);
        SOFT_CONSTRAINTS = soft.toArray(new SoftConstraint[soft.size()]);
    }

    public Environment(final String name, final Slot[] lectureSlots, final Slot[] labSlots, final NotCompatible[] notCompatibles, final Unwanted[] unwanted, final Preference[] preferences, final Pair[] pairs, final PartialAssign[] partialAssigns) {
        this.name = name;
        this.lectureSlots = lectureSlots;
        this.labSlots = labSlots;
        this.notCompatibles = notCompatibles;
        this.unwanted = unwanted;
        this.preferences = preferences;
        this.pairs = pairs;
        this.partialAssigns = partialAssigns;

        this.slots = Slot.getSlots();
        this.lectures = Lecture.getLectures();
        this.labs = Lab.getLabs();
        this.directory = new CourseDirectory(this);
    }

    public String getName() {
        return name;
    }

    public Slot[] getLabSlots() {
        return labSlots.clone();
    }

    public int getLabSlotCount() {
        return labSlots.length;
    }

    public Slot[] getLectureSlots() {
        return lectureSlots.clone();
    }

    public int getLectureSlotCount() {
        return lectureSlots.length;
    }

    public Slot[] getSlots() {
        return slots.clone();
    }

    public Slot getSlot(final int id) {
        return slots[id];
    }

    public int getSlotCount() {
        return slots.length;
    }

    public int getLabCount() {
        return labs.length;
    }

    public Lab[] getLabs() {
        return labs.clone();
    }

    public Lab getLab(final int id) {
        return labs[id];
    }

    public int getLectureCount() {
        return lectures.length;
    }

    public Lecture[] getLectures() {
        return lectures.clone();
    }

    public Lecture getLecture(final int id) {
        return lectures[id];
    }

    @Deprecated
    public NotCompatible[] getNotCompatibles() {
        return notCompatibles.clone();
    }

    @Deprecated
    public Unwanted[] getUnwanted() {
        return unwanted.clone();
    }

    @Deprecated
    public Preference[] getPreferences() {
        return preferences.clone();
    }

    @Deprecated
    public Pair[] getPairs() {
        return pairs.clone();
    }

    @Deprecated
    public PartialAssign[] getPartialAssigns() {
        return partialAssigns.clone();
    }

    public Course[] getNonCompatibles(final Course course) {
        // TODO
        return null;
    }

    public Preference[] getPreferences(final Course course) {
        // TODO
        return null;
    }

    public Course[] getPairs(final Course course) {
        // TODO
        return null;
    }

    public PartialAssign getPartialAssign(final Course course) {
        // TODO
        return null;
    }

    public HardConstraint[] getHardConstraints() {
        return hardConstraints.clone();
    }

    public SoftConstraint[] getSoftConstraints() {
        return softConstraints.clone();
    }

    public CourseDirectory getDirectory() {
        return directory;
    }
}
