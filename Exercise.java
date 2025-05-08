/**
 * Stores information of individual exercises. 
 * TODO Follow it with additional details about its purpose, what abstraction
 * it represents, and how to use it.
 *
 * @author  Jadon Chanthavong
 * @version March 24, 2025
 */
public class Exercise {
    private String name;
    private int sets;
    private int reps;
    private double duration;

    // Public exercise constructor
    public Exercise(String name, int sets, int reps, double duration) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.duration = duration;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getDuration() {
        return duration;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(int reps) {
       this.reps = reps;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }


}
