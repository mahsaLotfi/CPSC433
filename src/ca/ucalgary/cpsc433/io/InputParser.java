package ca.ucalgary.cpsc433.io;

import ca.ucalgary.cpsc433.environment.Course;
import ca.ucalgary.cpsc433.environment.Environment;
import ca.ucalgary.cpsc433.environment.Lab;
import ca.ucalgary.cpsc433.environment.Lecture;
import ca.ucalgary.cpsc433.environment.NotCompatible;
import ca.ucalgary.cpsc433.environment.Pair;
import ca.ucalgary.cpsc433.environment.PartialAssign;
import ca.ucalgary.cpsc433.environment.Preference;
import ca.ucalgary.cpsc433.environment.Unwanted;
import ca.ucalgary.cpsc433.schedule.Assign;
import ca.ucalgary.cpsc433.schedule.Day;
import ca.ucalgary.cpsc433.schedule.Slot;
import ca.ucalgary.cpsc433.schedule.Time;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Obicere
 */
public class InputParser {

    private final BufferedReader reader;

    private String name;

    private List<Slot> lectureSlots = new LinkedList<>();

    private List<Slot> labSlots = new LinkedList<>();

    private List<Lecture> lectures = new LinkedList<>();

    private List<Lab> labs = new LinkedList<>();

    private Map<Lecture, List<Lab>> labAssignments = new HashMap<>();

    private List<NotCompatible> notCompatibles = new LinkedList<>();

    private List<Unwanted> unwanted = new LinkedList<>();

    private List<Preference> preferences = new LinkedList<>();

    private List<Pair> pairs = new LinkedList<>();

    private List<PartialAssign> partialAssigns = new LinkedList<>();

    public InputParser(final BufferedReader reader) {
        this.reader = reader;
    }

    public static Environment parseEnvironment(final String inputFile) throws IOException {
        final File file = new File(inputFile);
        if (!file.exists()) {
            throw new IOException("No such file found: \"" + file.getAbsoluteFile() + "\"");
        }
        if (!file.isFile() || !file.canRead()) {
            throw new IOException("Must be a readable file: \"" + file.getAbsoluteFile() + "\"");
        }
        final FileReader fileReader = new FileReader(file);
        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        final InputParser parser = new InputParser(bufferedReader);

        parser.doParse();

        bufferedReader.close();

        parser.assignLabs();

        return parser.getEnvironment();
    }

    private void doParse() throws IOException {
        String next = reader.readLine();
        if (next == null) {
            throw new IOException("Input file must be non-empty.");
        }
        while (next != null) {
            next = next.trim();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                final CharBuffer buffer = new CharBuffer(line.trim());
                switch (next) {
                    case "Name:":
                        this.name = parseName(buffer);
                        break;
                    case "Course slots:":
                        final Slot lectureSlot = parseSlot(buffer, true);
                        if (lectureSlot.isValidLectureSlot()) {
                            lectureSlots.add(lectureSlot);
                        } else {
                            System.out.println("Warning: invalid lecture slot: " + lectureSlot);
                        }
                        break;
                    case "Lab slots:":
                        final Slot labSlot = parseSlot(buffer, false);
                        if (labSlot.isValidLabSlot()) {
                            labSlots.add(labSlot);
                        } else {
                            System.out.println("Warning: invalid lab slot: " + labSlot);
                        }
                        break;
                    case "Courses:":
                        final Lecture lecture = parseLecture(buffer);
                        lectures.add(lecture);
                        labAssignments.putIfAbsent(lecture, new LinkedList<>());
                        break;
                    case "Labs:":
                        final Lab lab = parseLab(buffer);
                        final List<Lab> assigns = labAssignments.get(lab.getLecture());
                        if (assigns == null) {
                            throw new AssertionError("Expected a lecture assignment list to exist before lab parsing.");
                        }
                        assigns.add(lab);
                        labs.add(lab);
                        break;
                    case "Not compatible:":
                        notCompatibles.add(parseNotCompatible(buffer));
                        break;
                    case "Unwanted:":
                        unwanted.add(parseUnwanted(buffer));
                        break;
                    case "Preferences:":
                        preferences.add(parsePreference(buffer));
                        break;
                    case "Pair:":
                        pairs.add(parsePair(buffer));
                        break;
                    case "Partial assignments:":
                        partialAssigns.add(parsePartialAssign(buffer));
                        break;
                    default:
                        throw new AssertionError("Unexpected input: " + line);
                }
                line = reader.readLine();
            }
            next = reader.readLine();
        }
    }

    private void assignLabs() {
        for (final Map.Entry<Lecture, List<Lab>> entry : labAssignments.entrySet()) {
            final Lecture lecture = entry.getKey();
            final List<Lab> labs = entry.getValue();
            final Lab[] labArray = labs.toArray(new Lab[labs.size()]);

            lecture.setLabs(labArray);
        }
    }

    private Environment getEnvironment() {
        final Slot[] newLectureSlots = lectureSlots.toArray(new Slot[lectureSlots.size()]);
        final Slot[] newLabSlots = labSlots.toArray(new Slot[labSlots.size()]);
        final Lecture[] newLectures = lectures.toArray(new Lecture[lectures.size()]);
        final Lab[] newLabs = labs.toArray(new Lab[labs.size()]);
        final NotCompatible[] newNotCompatibles = notCompatibles.toArray(new NotCompatible[notCompatibles.size()]);
        final Unwanted[] newUnwanted = unwanted.toArray(new Unwanted[unwanted.size()]);
        final Preference[] newPreferences = preferences.toArray(new Preference[preferences.size()]);
        final Pair[] newPairs = pairs.toArray(new Pair[pairs.size()]);
        final PartialAssign[] newPartialAssigns = partialAssigns.toArray(new PartialAssign[partialAssigns.size()]);

        return new Environment(name, newLectureSlots, newLabSlots, newLectures, newLabs, newNotCompatibles, newUnwanted, newPreferences, newPairs, newPartialAssigns);
    }

    private String parseName(final CharBuffer line) {
        return line.nextIdentifier();
    }

    private Slot parseSlot(final CharBuffer line, final boolean lecture) {
        final Day day = parseDay(line);
        line.skipComma();

        final Time time = parseTime(line);
        line.skipComma();

        final int max = line.nextInt();
        line.skipComma();

        final int min = line.nextInt();

        final Slot slot = Slot.getSlot(day, time);

        if (lecture) {
            slot.setLectureLimit(min, max);
        } else {
            slot.setLabLimit(min, max);
        }

        return slot;
    }

    private Course parseCourse(final CharBuffer line) {
        final String course = line.nextIdentifier();
        line.skipSpaces();

        final int number = line.nextInt();
        line.skipSpaces();

        final String type = line.nextIdentifier();
        line.skipSpaces();

        final int lectureSection;
        if (type.equals("LEC")) {
            lectureSection = line.nextInt();
        } else if (type.equals("TUT") || type.equals("LAB")) {
            lectureSection = 1;
        } else {
            throw new IllegalArgumentException("Failed to parse course type: " + type);
        }
        line.skipSpaces();

        final Lecture newLecture = new Lecture(course, number, lectureSection);
        final Lecture existing;
        final int index = lectures.indexOf(newLecture);
        if (index >= 0) {
            existing = lectures.get(index);
        } else {
            existing = newLecture;
        }
        if (line.hasNext() && line.peek() != ',') {
            final String labType;
            if (type.equals("LAB") || type.equals("TUT")) {
                labType = type;
            } else {
                labType = line.nextIdentifier();
                line.skipSpaces();
            }

            boolean isLab;
            if (labType.equals("LAB")) {
                isLab = true;
            } else if (labType.equals("TUT")) {
                isLab = false;
            } else {
                throw new IllegalArgumentException("Failed to parse lab type: " + type);
            }

            final int labSection = line.nextInt();
            line.skipSpaces();

            final Lab lab = new Lab(existing, isLab, labSection);
            final Lab existingLab;
            final int labIndex = labs.indexOf(lab);
            if (labIndex >= 0) {
                existingLab = labs.get(labIndex);
            } else {
                existingLab = lab;
            }
            return existingLab;
        } else {
            return existing;
        }
    }

    private Lecture parseLecture(final CharBuffer line) {
        return (Lecture) parseCourse(line);
    }

    private Lab parseLab(final CharBuffer line) {
        return (Lab) parseCourse(line);
    }

    private NotCompatible parseNotCompatible(final CharBuffer line) {
        final Course left = parseCourse(line);
        line.skipComma();
        final Course right = parseCourse(line);

        return new NotCompatible(left, right);
    }

    private Unwanted parseUnwanted(final CharBuffer line) {
        final Course course = parseCourse(line);
        line.skipComma();
        final Day day = parseDay(line);
        line.skipComma();
        final Time time = parseTime(line);

        return new Unwanted(Assign.getAssign(course, Slot.getSlot(day, time)));
    }

    private Preference parsePreference(final CharBuffer line) {
        final Day day = parseDay(line);
        line.skipComma();
        final Time time = parseTime(line);
        line.skipComma();
        final Course course = parseCourse(line);
        line.skipComma();
        final int value = line.nextInt();

        return new Preference(Assign.getAssign(course, Slot.getSlot(day, time)), value);
    }

    private Pair parsePair(final CharBuffer line) {
        final Course left = parseCourse(line);
        line.skipComma();
        final Course right = parseCourse(line);

        return new Pair(left, right);
    }

    private PartialAssign parsePartialAssign(final CharBuffer line) {
        final Course course = parseCourse(line);
        line.skipComma();
        final Day day = parseDay(line);
        line.skipComma();
        final Time time = parseTime(line);

        return new PartialAssign(Assign.getAssign(course, Slot.getSlot(day, time)));
    }

    private Time parseTime(final CharBuffer line) {
        final int hour = line.nextInt();
        final char c = line.next();
        if (c != ':') {
            throw new IllegalArgumentException("Expected a colon in date format: " + c);
        }
        final int minute = line.nextInt();
        return new Time(hour, minute);
    }

    private Day parseDay(final CharBuffer line) {
        final String day = line.nextIdentifier();
        switch (day) {
            case "MO":
                return Day.MONDAY;
            case "TU":
                return Day.TUESDAY;
            case "FR":
                return Day.FRIDAY;
            default:
                throw new AssertionError("Unknown day identifier: " + day);
        }
    }

    private static class CharBuffer {

        private int mark = -1;

        private char[] chars;

        private int read;

        public CharBuffer(final String input) {
            this.chars = input.toCharArray();
        }

        private void mark() {
            this.mark = read;
        }

        private String retrieve() {
            if (mark == -1) {
                throw new IllegalStateException("buffer must be marked before retrieval.");
            }
            final String value = new String(chars, mark, read - mark);
            mark = -1;
            return value;
        }

        private char peek() {
            return chars[read];
        }

        private char next() {
            return chars[read++];
        }

        private boolean hasNext() {
            return read < chars.length;
        }

        private void skipSpaces() {
            while (hasNext()) {
                if (peek() != ' ') {
                    break;
                }
                next();
            }
        }

        private void skipComma() {
            final char comma = next();
            if (comma != ',') {
                throw new IllegalStateException("Expected comma in the line, index: " + (read - 1));
            }
            skipSpaces();
        }

        private int nextInt() {
            int parse = 0;
            boolean negative = false;

            if (peek() == '-') {
                negative = true;
                next();
            }
            while (hasNext()) {
                if (!Character.isDigit(peek())) {
                    break;
                }
                parse = 10 * parse + (next() - '0');
            }
            return negative ? -parse : parse;
        }

        private String nextIdentifier() {
            mark();
            while (hasNext()) {
                final char peek = peek();
                if (!Character.isJavaIdentifierPart(peek)) {
                    break;
                }
                next();
            }
            return retrieve();
        }
    }

}

