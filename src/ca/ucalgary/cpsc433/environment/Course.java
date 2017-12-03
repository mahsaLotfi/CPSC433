package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public interface Course extends Comparable<Course> {

    public String getType();

    public int getNumber();

    public int getSection();

    public boolean isLecture();

    public int getID();

}
