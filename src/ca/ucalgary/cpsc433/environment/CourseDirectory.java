package ca.ucalgary.cpsc433.environment;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Obicere
 */
public class CourseDirectory {

    private final Map<String, Course[]> idToCourses;

    private final Map<String, Lecture[]> idToLectures;

    private final Map<String, Lab[]> idToLabs;

    public CourseDirectory(final Environment environment) {
        if (environment == null) {
            throw new NullPointerException("environment must be non-null");
        }
        final Map<String, Set<Course>> courses = new HashMap<>();
        final Map<String, Set<Lecture>> lectures = new HashMap<>();
        final Map<String, Set<Lab>> labs = new HashMap<>();

        for (final Lecture lecture : environment.getLectures()) {
            insert(courses, lecture);
            insert(lectures, lecture);
        }
        for(final Lab lab : environment.getLabs()) {
            insert(courses, lab);
            insert(labs, lab);
        }

        this.idToCourses = flatten(courses, Course.class);
        this.idToLectures = flatten(lectures, Lecture.class);
        this.idToLabs = flatten(labs, Lab.class);
    }

    private <T extends Course> void insert(final Map<String, Set<T>> map, final T t) {
        final String id = getID(t);

        final Set<T> existing = map.get(id);
        if (existing != null) {
            existing.add(t);
        } else {
            final Set<T> newSet = new LinkedHashSet<>();
            newSet.add(t);
            map.put(id, newSet);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Course> Map<String, T[]> flatten(final Map<String, Set<T>> map, final Class<T> cls) {
        final Map<String, T[]> newMap = new ConcurrentHashMap<>();
        for(final Map.Entry<String, Set<T>> entry : map.entrySet()) {
            final Set<T> value = entry.getValue();
            final T[] array = (T[]) Array.newInstance(cls, value.size());
            newMap.put(entry.getKey(), value.toArray(array));
        }
        return newMap;
    }

    public Course[] getCourses(final String type, final int number) {
        if (type == null) {
            throw new NullPointerException("type must be non-null");
        }
        return idToCourses.get(getID(type, number)).clone();
    }

    public Lecture[] getLectures(final String type, final int number) {
        if (type == null) {
            throw new NullPointerException("type must be non-null");
        }
        return idToLectures.get(getID(type, number)).clone();
    }

    public Lab[] getLabs(final String type, final int number) {
        if (type == null) {
            throw new NullPointerException("type must be non-null");
        }
        return idToLabs.get(getID(type, number)).clone();
    }

    private String getID(final Course course) {
        return getID(course.getType(), course.getNumber());
    }

    private String getID(final String type, final int number) {
        return type + number;
    }
}
