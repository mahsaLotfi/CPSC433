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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Parses an input file into an environment. Requires strict form and all
 * specifier tags must exist. Single use only.
 *
 * @author Obicere
 * @see InputParser#parseEnvironment(String)
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

    private InputParser(final BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Parses the input file to form a new environment instance. The format
     * must follow the specified format, with all specifier tags existing.
     *
     * @param inputFile The input file to parse.
     * @return The new environment instance represented by this file.
     * @throws IOException If the file does not exist, cannot be read
     *                     from,
     *                     or an error occurs while parsing the file.
     */
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

        // parse the file
        parser.doParse();

        // close the file
        bufferedReader.close();

        // collapse the labs
        parser.assignLabs();

        // ensure that CPSC 813 and 913 exist if needed.
        parser.ensureCPSCX13Exists();

        // retrieve the environment
        return parser.getEnvironment();
    }

    /**
     * Parses the entire file, until no more content is available.
     *
     * @throws IOException If there is an exception during parsing.
     */
    private void doParse() throws IOException {
        String next = reader.readLine();
        if (next == null) {
            throw new IOException("Input file must be non-empty.");
        }
        // parse the next specifier
        while (next != null) {
            next = next.trim();

            String line = reader.readLine();
            // parse all the content below the specifier
            while (line != null && !line.isEmpty()) {
                final CharBuffer buffer = new CharBuffer(line.trim());

                parseSpecifier(next, buffer);

                // next content line
                line = reader.readLine();
            }
            // next specifier
            next = reader.readLine();
        }
    }

    /**
     * Assigns all the labs to the lectures they belong too. A lab may be
     * assigned to more than one lecture.
     */
    private void assignLabs() {
        for (final Map.Entry<Lecture, List<Lab>> entry : labAssignments.entrySet()) {
            final Lecture lecture = entry.getKey();
            final List<Lab> labs = entry.getValue();
            final Lab[] labArray = labs.toArray(new Lab[labs.size()]);

            lecture.setLabs(labArray);
        }
    }

    /**
     * Compacts the information into the environment.
     *
     * @return The new environment instance.
     */
    private Environment getEnvironment() {
        final Slot[] newLectureSlots = lectureSlots.toArray(new Slot[lectureSlots.size()]);
        final Slot[] newLabSlots = labSlots.toArray(new Slot[labSlots.size()]);
        final NotCompatible[] newNotCompatibles = notCompatibles.toArray(new NotCompatible[notCompatibles.size()]);
        final Unwanted[] newUnwanted = unwanted.toArray(new Unwanted[unwanted.size()]);
        final Preference[] newPreferences = preferences.toArray(new Preference[preferences.size()]);
        final Pair[] newPairs = pairs.toArray(new Pair[pairs.size()]);
        final PartialAssign[] newPartialAssigns = partialAssigns.toArray(new PartialAssign[partialAssigns.size()]);

        return new Environment(name, newLectureSlots, newLabSlots, newNotCompatibles, newUnwanted, newPreferences, newPairs, newPartialAssigns);
    }

    /**
     * Ensures that the CPSC 813 and 913 classes exist, if necessary. The
     * slot at Tuesday at 18:00 is also added, with only enough lecture
     * slots for CPSC 813 and CPSC 913 (if they exist).
     * <p>
     * A partial assignment is also generated for the class, to allow quick
     * optimization.
     */
    private void ensureCPSCX13Exists() {
        final List<Lecture> cpsc313 = getLectures("CPSC", 313, -1);
        final List<Lecture> cpsc413 = getLectures("CPSC", 413, -1);
        final List<Lecture> cpsc813 = getLectures("CPSC", 813, -1);
        final List<Lecture> cpsc913 = getLectures("CPSC", 913, -1);
        if (!cpsc313.isEmpty() && cpsc813.isEmpty()) {
            final Lecture newLecture = Lecture.getLecture("CPSC", 813, 1);
            newLecture.setLabs(new Lab[0]);
            final Slot newSlot = Slot.getSlot(Day.TUESDAY, new Time(18, 0));
            newSlot.setLectureLimit(0, newSlot.getLectureMax() + 1);
            lectureSlots.add(newSlot);

            partialAssigns.add(new PartialAssign(Assign.getAssign(newLecture, newSlot)));
        }
        if (!cpsc413.isEmpty() && cpsc913.isEmpty()) {
            final Lecture newLecture = Lecture.getLecture("CPSC", 913, 1);
            newLecture.setLabs(new Lab[0]);
            final Slot newSlot = Slot.getSlot(Day.TUESDAY, new Time(18, 0));
            newSlot.setLectureLimit(0, newSlot.getLectureMax() + 1);
            lectureSlots.add(newSlot);

            final PartialAssign assign = new PartialAssign(Assign.getAssign(newLecture, newSlot));
            partialAssigns.add(assign);
        }
    }

    /**
     * Parses the content based on the given specifier. The valid
     * specifiers are:
     * <ol>
     * <li>Name:</li>
     * <li>Course slots:</li>
     * <li>Lab slots:</li>
     * <li>Courses:</li>
     * <li>Labs:</li>
     * <li>Not compatible:</li>
     * <li>Unwanted:</li>
     * <li>Preferences:</li>
     * <li>Pair:</li>
     * <li>Partial assignments:</li>
     * </ol>
     *
     * @param specifier The specifier for the content type
     * @param buffer    The line containing the new content
     */
    private void parseSpecifier(final String specifier, final CharBuffer buffer) {
        switch (specifier) {
            case "Name:":
                parseNameSpecifier(buffer);
                break;
            case "Course slots:":
                parseLectureSlotSpecifier(buffer);
                break;
            case "Lab slots:":
                parseLabSlotSpecifier(buffer);
                break;
            case "Courses:":
                parseLectureSpecifier(buffer);
                break;
            case "Labs:":
                parseLabSpecifier(buffer);
                break;
            case "Not compatible:":
                parseNotCompatibleSpecifier(buffer);
                break;
            case "Unwanted:":
                parseUnwantedSpecifier(buffer);
                break;
            case "Preferences:":
                parsePreferenceSpecifier(buffer);
                break;
            case "Pair:":
                parsePairSpecifier(buffer);
                break;
            case "Partial assignments:":
                parsePartialAssignSpecifier(buffer);
                break;
            default:
                throw new AssertionError("Unexpected input: " + buffer);
        }
    }

    /**
     * Parses name content.
     *
     * @param line The content line.
     */
    private void parseNameSpecifier(final CharBuffer line) {
        this.name = line.nextIdentifier();
    }

    /**
     * Parses a new lecture slot.
     *
     * @param line The content line.
     */
    private void parseLectureSlotSpecifier(final CharBuffer line) {
        final Slot lectureSlot = parseSlot(line, true);
        if (lectureSlot.isValidLectureSlot()) {
            lectureSlots.add(lectureSlot);
        } else {
            System.out.println("Warning: invalid lecture slot: " + lectureSlot);
        }
    }

    /**
     * Parses a new lab slot.
     *
     * @param line The content line.
     */
    private void parseLabSlotSpecifier(final CharBuffer line) {
        final Slot labSlot = parseSlot(line, false);
        if (labSlot.isValidLabSlot()) {
            labSlots.add(labSlot);
        } else {
            System.out.println("Warning: invalid lab slot: " + labSlot);
        }
    }

    /**
     * Parses a slot.
     *
     * @param line The content line.
     */
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

    /**
     * Parses a lecture.
     *
     * @param line The content line.
     */
    private void parseLectureSpecifier(final CharBuffer line) {
        final Lecture lecture = parseLecture(line);
        lectures.add(lecture);
        labAssignments.putIfAbsent(lecture, new LinkedList<>());
    }

    /**
     * Parses a lab. Assigns the lectures associated to this lab.
     *
     * @param line The content line.
     */
    private void parseLabSpecifier(final CharBuffer line) {
        final Lab lab = parseLab(line);
        final Lecture[] lectures = lab.getLectures();
        for (final Lecture labLecture : lectures) {
            final List<Lab> assigns = labAssignments.get(labLecture);
            if (assigns == null) {
                throw new AssertionError("Expected a lecture assignment list to exist before lab parsing.");
            }
            assigns.add(lab);
        }
        labs.add(lab);
    }

    /**
     * Parses a new lecture.
     *
     * @param line The content line.
     */
    private Lecture parseLecture(final CharBuffer line) {
        return (Lecture) parseCourse(line);
    }

    /**
     * Parses a new lab.
     *
     * @param line The content line.
     */
    private Lab parseLab(final CharBuffer line) {
        return (Lab) parseCourse(line);
    }

    /**
     * Parses a course. This will create any new courses as necessary. Can
     * deal with any of the following forms:
     * <pre>{@code
     * type number LEC section
     * type number LEC section LAB section
     * type number LEC section TUT section
     * type number LAB section
     * type number TUT section
     * }</pre>
     *
     * @param line The content line.
     */
    private Course parseCourse(final CharBuffer line) {
        final String course = line.nextIdentifier();
        line.skipSpaces();

        final int number = line.nextInt();
        line.skipSpaces();

        final String type = line.nextIdentifier();
        line.skipSpaces();

        final int lectureSection;
        // determine if there is a lecture specifier or tutorial specifier.
        if (type.equals("LEC")) {
            lectureSection = line.nextInt();
        } else if (type.equals("TUT") || type.equals("LAB")) {
            lectureSection = -1;
        } else {
            throw new IllegalArgumentException("Failed to parse course type: " + type);
        }
        line.skipSpaces();

        // if there are existing lectures for the given (type, number, section),
        // then operate on these. This avoids duplicate entries as well.
        final List<Lecture> lectures = getLectures(course, number, lectureSection);
        final Lecture[] lecture;
        if (!lectures.isEmpty()) {
            lecture = lectures.toArray(new Lecture[lectures.size()]);
        } else {
            if (lectureSection == -1) {
                throw new AssertionError("could not have reached ambiguous lecture before lecture initialized");
            }
            lecture = new Lecture[]{
                    Lecture.getLecture(course, number, lectureSection)
            };
        }

        // there is a tutorial specifier following
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

            final Lab lab = Lab.getLab(lecture, isLab, labSection);
            final Lab existingLab;
            final int labIndex = labs.indexOf(lab);
            if (labIndex >= 0) {
                existingLab = labs.get(labIndex);
            } else {
                existingLab = lab;
            }
            return existingLab;
        } else {
            if (lecture.length > 1) {
                throw new AssertionError("should not have reached here if only 1 lecture");
            }
            return lecture[0];
        }
    }

    /**
     * Parses a new not-compatible specifier.
     *
     * @param line The content line.
     */
    private void parseNotCompatibleSpecifier(final CharBuffer line) {
        final Course left = parseCourse(line);
        line.skipComma();
        final Course right = parseCourse(line);

        final NotCompatible compatible = new NotCompatible(left, right);
        notCompatibles.add(compatible);
    }

    /**
     * Parses a new unwanted specifier. If the slot is invalid, no unwanted
     * is parsed.
     *
     * @param line The content line.
     */
    private void parseUnwantedSpecifier(final CharBuffer line) {
        final Course course = parseCourse(line);
        line.skipComma();
        final Day day = parseDay(line);
        line.skipComma();
        final Time time = parseTime(line);

        // no point caring about it, if the slot can't be assigned to
        if (!Slot.exists(day, time)) {
            return;
        }

        final Unwanted unwanted = new Unwanted(Assign.getAssign(course, Slot.getSlot(day, time)));
        this.unwanted.add(unwanted);
    }

    /**
     * Parses a new preference specifier. If the slot is invalid, no
     * preference is parsed.
     *
     * @param line The content line.
     */
    private void parsePreferenceSpecifier(final CharBuffer line) {
        final Day day = parseDay(line);
        line.skipComma();
        final Time time = parseTime(line);
        line.skipComma();
        final Course course = parseCourse(line);
        line.skipComma();
        final int value = line.nextInt();

        // no point caring about it, if the slot can't be assigned to
        if (!Slot.exists(day, time)) {
            return;
        }

        final Preference preference = new Preference(Assign.getAssign(course, Slot.getSlot(day, time)), value);
        preferences.add(preference);
    }

    /**
     * Parses a new pair specifier.
     *
     * @param line The content line.
     */
    private void parsePairSpecifier(final CharBuffer line) {
        final Course left = parseCourse(line);
        line.skipComma();
        final Course right = parseCourse(line);

        final Pair pair = new Pair(left, right);
        pairs.add(pair);
    }

    /**
     * Parses a new partial assignment. If the slot is invalid, no
     * assignment is parsed.
     *
     * @param line The content line.
     */
    private void parsePartialAssignSpecifier(final CharBuffer line) {
        final Course course = parseCourse(line);
        line.skipComma();
        final Day day = parseDay(line);
        line.skipComma();
        final Time time = parseTime(line);

        // no point caring about it, if the slot can't be assigned to
        if (!Slot.exists(day, time)) {
            return;
        }

        final PartialAssign partialAssign = new PartialAssign(Assign.getAssign(course, Slot.getSlot(day, time)));
        partialAssigns.add(partialAssign);
    }

    /**
     * Parses a new time specifier. The format is: <code>HH:MM</code>.
     *
     * @param line The content line.
     */
    private Time parseTime(final CharBuffer line) {
        final int hour = line.nextInt();
        final char c = line.next();
        if (c != ':') {
            throw new IllegalArgumentException("Expected a colon in date format: " + c);
        }
        final int minute = line.nextInt();
        return new Time(hour, minute);
    }

    /**
     * Parses a new {@link Day}. Must be one of <code>MO</code>,
     * <code>TU</code>, <code>FR</code>.
     *
     * @param line The content line.
     */
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

    /**
     * Retrieves all the lectures with the given type, number and section.
     * If the section is not specified, multiple lectures may be returned.
     *
     * @param type    The course type to retrieve.
     * @param number  The course number to retrieve.
     * @param section The course section to retrieve. If <code>-1</code>,
     *                all matching lectures are returned.
     */
    private List<Lecture> getLectures(final String type, final int number, final int section) {
        final List<Lecture> lectures = new ArrayList<>();
        for (final Lecture lecture : this.lectures) {
            if (lecture.getType().equals(type) && lecture.getNumber() == number) {
                if (section == -1 || section == lecture.getSection()) {
                    lectures.add(lecture);
                }
            }
        }
        return lectures;
    }

    /**
     * Character buffer for parsing. Used to provide easy parsing methods
     * for lookup,
     * peeking and processing.
     */
    private static class CharBuffer {

        private int mark = -1;

        private char[] chars;

        private int read;

        public CharBuffer(final String input) {
            this.chars = input.toCharArray();
        }

        /**
         * Marks the current position.
         */
        private void mark() {
            this.mark = read;
        }

        /**
         * Retrieves the current content from the mark to the current
         * index. Requires a call to <code>mark</code> before this.
         *
         * @return The substring from <code>(mark, current)</code>.
         */
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

        /**
         * Skips all spaces in the content.
         */
        private void skipSpaces() {
            while (hasNext()) {
                if (peek() != ' ') {
                    break;
                }
                next();
            }
        }

        /**
         * Parses a comma, then skips all following spaces.
         */
        private void skipComma() {
            final char comma = next();
            if (comma != ',') {
                throw new IllegalStateException("Expected comma in the line, index: " + (read - 1));
            }
            skipSpaces();
        }

        /**
         * Parses a decimal integer. Does not provide overflow protection.
         *
         * @return The integer parsed from the input.
         */
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

        /**
         * Parses the next identifier. A valid identifier character is any
         * alphabetical, digit, or underscore.
         *
         * @return The parsed identifier.
         */
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

