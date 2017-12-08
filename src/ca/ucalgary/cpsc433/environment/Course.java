package ca.ucalgary.cpsc433.environment;

/**
 * Course Interface. Each course will be known with a type, number, section, 
 * ID and whether it's a Lecture or Lab.
 *  
 * @author Obicere
 */
public interface Course extends Comparable<Course> {

    public String getType();

    public int getNumber();

    public int getSection();

    public boolean isLecture();

    public int getID();

}
