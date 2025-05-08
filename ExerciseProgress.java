/**
 * Tracks the progress of a specific exercise during a workout session.
 * This class maintains information about completed sets, reps, and
 * whether an exercise has been completed.
 *
 * @author  Jadon Chanthavong
 * @version April 09, 2025
 */

public class ExerciseProgress {
    private Exercise exercise;
    private int completedSets;
    private int completedReps;
    private boolean isCompleted;

     /**
     * Creates a new ExerciseProgress for the specified exercise.
     *
     * @param exercise the exercise to track progress for
     */
    public ExerciseProgress(Exercise exercise) {
        this.exercise = exercise;
        this.completedSets = 0;
        this.completedReps = 0;
        this.isCompleted = false;
    }
    
    /**
     * Updates the progress for this exercise.
     *
     * @param completedSets the number of sets completed
     * @param completedReps the number of reps completed in the current set
     */
    public void updateProgress(int completedSets, int completedReps) {
        this.completedSets = completedSets;
        this.completedReps = completedReps;
        
        // Check if exercise is completed
        if (completedSets >= exercise.getSets()) {
            this.isCompleted = true;
        }
    }
    
    /**
     * Marks this exercise as completed regardless of sets/reps.
     */
    public void markCompleted() {
        this.isCompleted = true;
        this.completedSets = exercise.getSets();
        this.completedReps = exercise.getReps();
    }
    
    /**
     * Calculates the completion percentage of this exercise.
     *
     * @return the percentage of completion (0-100)
     */
    public double getCompletionPercentage() {
        if (isCompleted) {
            return 100.0;
        }
        
        double totalReps = exercise.getSets() * exercise.getReps();
        double completedTotalReps = (completedSets * exercise.getReps()) + completedReps;
        
        return (completedTotalReps / totalReps) * 100.0;
    }
    
    /**
     * Gets the number of remaining sets for this exercise.
     *
     * @return the number of remaining sets
     */
    public int getRemainingSetCount() {
        return Math.max(0, exercise.getSets() - completedSets);
    }
    
    /**
     * Checks if this exercise is completed.
     *
     * @return true if the exercise is completed, false otherwise
     */
    public boolean isCompleted() {
        return isCompleted;
    }
    
    /**
     * Gets the exercise being tracked.
     *
     * @return the exercise
     */
    public Exercise getExercise() {
        return exercise;
    }
    
    /**
     * Gets the number of completed sets.
     *
     * @return the completed sets
     */
    public int getCompletedSets() {
        return completedSets;
    }
    
    /**
     * Gets the number of completed reps in the current set.
     *
     * @return the completed reps
     */
    public int getCompletedReps() {
        return completedReps;
    }
    
    /**
     * Returns a string representation of the progress for this exercise.
     *
     * @return a string showing exercise name and completion status
     */
    @Override
    public String toString() {
        String status = isCompleted ? "COMPLETED" : 
                       String.format("%d/%d sets, %d/%d reps in current set (%.1f%%)", 
                       completedSets, exercise.getSets(), 
                       completedReps, exercise.getReps(),
                       getCompletionPercentage());
        
        return exercise.getName() + ": " + status;
    }
}
