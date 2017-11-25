package ca.ucalgary.cpsc433.environment;

/**
 * @author Obicere
 */
public interface Course extends Comparable<Course> {

    public String getName();

    public int getNumber();

}
