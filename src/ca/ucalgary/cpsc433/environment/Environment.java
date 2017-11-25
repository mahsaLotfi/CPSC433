package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public class Environment {

    private final Lab[] labs;

    private final Lecture[] lectures;

    private final NotCompatible[] notCompatibles;

    private final Pair[] pairs;

    private final PartialAssign[] partialAssigns;

    private final Preference[] preferences;

    private final Unwanted[] unwanted;

    public Environment(final Lecture[] lectures, final Lab[] labs, final NotCompatible[] notCompatibles, final Pair[] pairs, final PartialAssign[] partialAssigns, final Preference[] preferences, final Unwanted[] unwanted) {
        this.labs = labs;
        this.lectures = lectures;
        this.notCompatibles = notCompatibles;
        this.pairs = pairs;
        this.partialAssigns = partialAssigns;
        this.preferences = preferences;
        this.unwanted = unwanted;
    }

    public int getLabCount() {
        return labs.length;
    }

    public Lab[] getLabs() {
        return labs.clone();
    }

    public int getLectureCount() {
        return lectures.length;
    }

    public Lecture[] getLectures() {
        return lectures.clone();
    }

    public NotCompatible[] getNotCompatibles() {
        return notCompatibles.clone();
    }

    public Pair[] getPairs() {
        return pairs.clone();
    }

    public PartialAssign[] getPartialAssigns() {
        return partialAssigns.clone();
    }

    public Preference[] getPreferences() {
        return preferences.clone();
    }

    public Unwanted[] getUnwanted() {
        return unwanted.clone();
    }
}
