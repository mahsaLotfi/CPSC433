package ca.ucalgary.cpsc433.environment;

import ca.ucalgary.cpsc433.constraint.hard.CPSC813Constraint;
import ca.ucalgary.cpsc433.constraint.hard.CPSC913Constraint;
import ca.ucalgary.cpsc433.constraint.hard.FifthYearConstraint;
import ca.ucalgary.cpsc433.constraint.hard.HardConstraint;
import ca.ucalgary.cpsc433.constraint.hard.MaxConstraint;
import ca.ucalgary.cpsc433.constraint.hard.PartialAssignmentConstraint;
import ca.ucalgary.cpsc433.constraint.hard.TuesdayElevenConstraint;
import ca.ucalgary.cpsc433.constraint.hard.EveningConstraint;
import ca.ucalgary.cpsc433.constraint.soft.MinConstraint;
import ca.ucalgary.cpsc433.constraint.soft.SoftConstraint;
import ca.ucalgary.cpsc433.schedule.Slot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Obicere
 */
public class Environment {

    private static final Course[] EMPTY_COURSE = new Course[0];

    private static final Unwanted[] EMPTY_UNWANTED = new Unwanted[0];

    private static final Preference[] EMPTY_PREFERENCE = new Preference[0];

    private static final HardConstraint[] HARD_CONSTRAINTS;

    private static final SoftConstraint[] SOFT_CONSTRAINTS;

    private final String name;

    private final Slot[] labSlots;

    private final Slot[] lectureSlots;

    private final Slot[] slots;

    private final Lab[] labs;

    private final Lecture[] lectures;

    private final Course[] courses;

    private final HardConstraint[] hardConstraints = HARD_CONSTRAINTS;

    private final SoftConstraint[] softConstraints = SOFT_CONSTRAINTS;

    private final Course[][] notCompatibles;

    private final int notCompatibleCount;

    private final Unwanted[][] unwanted;

    private final int unwantedCount;

    private final Preference[][] preferences;

    private final int preferenceCount;

    private final Course[][] pairs;

    private final int pairsCount;

    private final PartialAssign[] partialAssigns;

    private final int partialAssignsCount;

    private final CourseDirectory directory;

    static {
        final List<HardConstraint> hard = new ArrayList<>();
        hard.add(new MaxConstraint());
        hard.add(new PartialAssignmentConstraint());
        hard.add(new FifthYearConstraint());
        hard.add(new EveningConstraint());
        hard.add(new TuesdayElevenConstraint());
        hard.add(new CPSC813Constraint());
        hard.add(new CPSC913Constraint());

        final List<SoftConstraint> soft = new ArrayList<>();

        soft.add(new MinConstraint());

        HARD_CONSTRAINTS = hard.toArray(new HardConstraint[hard.size()]);
        SOFT_CONSTRAINTS = soft.toArray(new SoftConstraint[soft.size()]);
    }

    public Environment(final String name, final Slot[] lectureSlots, final Slot[] labSlots, final NotCompatible[] notCompatibles, final Unwanted[] unwanted, final Preference[] preferences, final Pair[] pairs, final PartialAssign[] partialAssigns) {
        this.name = name;
        this.lectureSlots = lectureSlots;
        this.labSlots = labSlots;

        this.notCompatibleCount = notCompatibles.length;
        this.unwantedCount = unwanted.length;
        this.preferenceCount = preferences.length;
        this.pairsCount = pairs.length;
        this.partialAssignsCount = partialAssigns.length;

        this.slots = Slot.getSlots();
        this.lectures = Lecture.getLectures();
        this.labs = Lab.getLabs();

        this.courses = new Course[lectures.length + labs.length];

        System.arraycopy(lectures, 0, courses, 0, lectures.length);
        System.arraycopy(labs, 0, courses, lectures.length, labs.length);

        this.directory = new CourseDirectory(this);

        this.notCompatibles = buildNotCompatibles(notCompatibles);
        this.unwanted = buildUnwanted(unwanted);
        this.preferences = buildPreferences(preferences);
        this.pairs = buildPairs(pairs);
        this.partialAssigns = buildPartialAssigns(partialAssigns);
    }

    private Course[][] buildNotCompatibles(final NotCompatible[] notCompatibles) {
        final Course[][] value = new Course[courses.length][];
        for (int i = 0; i < courses.length; i++) {
            final Course course = getCourse(i);
            final List<Course> list = new LinkedList<>();
            for (final NotCompatible notCompatible : notCompatibles) {
                if (notCompatible.getRight().equals(course)) {
                    list.add(notCompatible.getLeft());
                } else if (notCompatible.getLeft().equals(course)) {
                    list.add(notCompatible.getRight());
                }
            }
            value[i] = list.toArray(new Course[list.size()]);
        }
        return value;
    }

    private Unwanted[][] buildUnwanted(final Unwanted[] unwanteds) {
        final Unwanted[][] value = new Unwanted[courses.length][];
        for (int i = 0; i < courses.length; i++) {
            final Course course = getCourse(i);
            final List<Unwanted> list = new LinkedList<>();
            for (final Unwanted unwanted : unwanteds) {
                if (unwanted.getAssign().getCourse().equals(course)) {
                    list.add(unwanted);
                }
            }
            value[i] = list.toArray(new Unwanted[list.size()]);
        }
        return value;
    }

    private Preference[][] buildPreferences(final Preference[] preferences) {
        final Preference[][] value = new Preference[courses.length][];
        for (int i = 0; i < courses.length; i++) {
            final Course course = getCourse(i);
            final List<Preference> list = new LinkedList<>();
            for (final Preference preference : preferences) {
                if (preference.getAssign().getCourse().equals(course)) {
                    list.add(preference);
                }
            }
            value[i] = list.toArray(new Preference[list.size()]);
        }
        return value;
    }

    private Course[][] buildPairs(final Pair[] pairs) {
        final Course[][] value = new Course[courses.length][];
        for (int i = 0; i < courses.length; i++) {
            final Course course = getCourse(i);
            final List<Course> list = new LinkedList<>();
            for (final Pair pair : pairs) {
                if (pair.getRight().equals(course)) {
                    list.add(pair.getLeft());
                } else if (pair.getLeft().equals(course)) {
                    list.add(pair.getRight());
                }
            }
            value[i] = list.toArray(new Course[list.size()]);
        }
        return value;
    }

    private PartialAssign[] buildPartialAssigns(final PartialAssign[] partialAssigns) {
        final PartialAssign[] value = new PartialAssign[courses.length];
        for (final PartialAssign partialAssign : partialAssigns) {
            final Course course = partialAssign.getAssign().getCourse();
            final int id = getCourseID(course);

            value[id] = partialAssign;
        }
        return value;
    }

    public String getName() {
        return name;
    }

    public Slot[] getLabSlots() {
        return labSlots;
    }

    public int getLabSlotCount() {
        return labSlots.length;
    }

    public Slot[] getLectureSlots() {
        return lectureSlots;
    }

    public int getLectureSlotCount() {
        return lectureSlots.length;
    }

    public Slot[] getSlots() {
        return slots;
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
        return labs;
    }

    public Lab getLab(final int id) {
        return labs[id];
    }

    public int getLectureCount() {
        return lectures.length;
    }

    public Lecture[] getLectures() {
        return lectures;
    }

    public Lecture getLecture(final int id) {
        return lectures[id];
    }

    public Course[] getCourses() {
        return courses;
    }

    public int getCourseCount() {
        return courses.length;
    }

    public Course getCourse(final int id) {
        return courses[id];
    }

    public int getCourseID(final Course course) {
        if (course.isLecture()) {
            return course.getID();
        } else {
            return course.getID() + lectures.length;
        }
    }

    public Course[] getNonCompatibles(final Course course) {
        final int id = getCourseID(course);
        final Course[] result = notCompatibles[id];
        if (result == null) {
            return EMPTY_COURSE;
        } else {
            return result;
        }
    }

    public boolean hasNonCompatibles() {
        return notCompatibleCount > 0;
    }

    public Unwanted[] getUnwanted(final Course course) {
        final int id = getCourseID(course);
        final Unwanted[] result = unwanted[id];
        if (result == null) {
            return EMPTY_UNWANTED;
        } else {
            return result;
        }
    }

    public boolean hasUnwanted() {
        return unwantedCount > 0;
    }

    public Preference[] getPreferences(final Course course) {
        final int id = getCourseID(course);
        final Preference[] result = preferences[id];
        if (result == null) {
            return EMPTY_PREFERENCE;
        } else {
            return result;
        }
    }

    public boolean hasPreferences() {
        return preferenceCount > 0;
    }

    public Course[] getPairs(final Course course) {
        final int id = getCourseID(course);
        final Course[] result = pairs[id];
        if (result == null) {
            return EMPTY_COURSE;
        } else {
            return result;
        }
    }

    public boolean hasPairs() {
        return pairsCount > 0;
    }

    public PartialAssign getPartialAssign(final Course course) {
        final int id = getCourseID(course);
        return partialAssigns[id];
    }

    public boolean hasPartialAssigns() {
        return partialAssignsCount > 0;
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

    public Course[] getCourses(final String type, final int number) {
        return directory.getCourses(type, number);
    }

    public Lecture[] getLectures(final String type, final int number) {
        return directory.getLectures(type, number);
    }

    public Lab[] getLabs(final String type, final int number) {
        return directory.getLabs(type, number);
    }
}
