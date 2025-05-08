import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a collection of exercises that form a complete workout routine.
 * The Workout class manages adding, removing, and organizing exercises,
 * as well as calculating total workout time and managing custom rest periods.
 *
 * @author  Jadon Chanthavong
 * @version Arpil 17, 2025
 */
public class Workout {
    private String name;
    private List<Exercise> exercises;
    private Map<String, Integer> customRestTimes;
    private Date creationDate;

    /**
     * Creates a new Workout with the specified name.
     * 
     * @param name the name of the workout
     */
    public Workout(String name) {
        this.name = name;
        this.exercises = new ArrayList<>();
        this.customRestTimes = new HashMap<>();
        this.creationDate = new Date();
    }

     /**
     * Adds an exercise to this workout.
     * 
     * @param exercise the exercise to add
     */
     public void addExercise(Exercise exercise) {
         exercises.add(exercise);
     }
    
    /**
     * Removes an exercise from this workout by name.
     * 
     * @param name the name of the exercise to remove
     * @return true if the exercise was found and removed, false otherwise
     */
    public boolean removeExercise(String name) {
        return exercises.removeIf(exercise -> exercise.getName().equalsIgnoreCase(name));
    }

     /**
     * Returns a list of all exercises in this workout.
     * 
     * @return a list of exercises
     */
     public List<Exercise> listExercises() {
         return new ArrayList<>(exercises); // Return a copy to prevent modification
     }
    
     /**
     * Calculates the total estimated duration of the workout in minutes,
     * including exercise durations and default rest times.
     * 
     * @return the total workout duration in minutes
     */
     public double calculateTotalWorkoutTime() {
         double totalTime = 0;

         for (Exercise exercise : exercises) {
             // Add exercise duration
             totalTime += (exercise.getDuration() / 60.0) * exercise.getSets();

             // Add rest time between sets
             String exerciseName = exercise.getName();
             int restTime = customRestTimes.getOrDefault(exerciseName, 60);

             // Rest between sets (one less than total sets)
             if (exercise.getSets() > 1) {
                 totalTime += (exercise.getSets() - 1) * (restTime / 60.0); // Convert seconds to minutes
             }
         }
         return totalTime;
     }
    
     /**
     * Sorts the exercises in this workout based on the specified criteria.
     * 
     * @param sortBy the criteria to sort by ("name", "sets", "reps", or "duration")
     */
     public void sortExercises(String sortBy) {
         switch (sortBy.toLowerCase()) {
             case "name":
                 Collections.sort(exercises, Comparator.comparing(e -> e.getName().toLowerCase()));
                 break;
             case "sets":
                 Collections.sort(exercises, Comparator.comparing(Exercise::getSets));
                 break;
             case "reps":
                 Collections.sort(exercises, Comparator.comparing(Exercise::getReps));
                 break;
             case "duration":
                 Collections.sort(exercises, Comparator.comparing(Exercise::getDuration));
                 break;
             default:
                 System.out.println("Invalid sort criteria. No changes made.");
         }
     }
    
     /**
     * Filters exercises based on parameters like duration or rep count.
     * 
     * @param filterBy the parameter to filter by ("sets", "reps", or "duration")
     * @param threshold the minimum value to include
     * @return a list of exercises that meet the criteria
     */
     public List<Exercise> filterExercises(String filterBy, int threshold) {
         List<Exercise> filtered = new ArrayList<>();

         for (Exercise exercise : exercises) {
             switch (filterBy.toLowerCase()) {
                 case "sets":
                     if (exercise.getSets() >= threshold) {
                         filtered.add(exercise);
                     }
                     break;
                 case "reps":
                     if (exercise.getReps() >= threshold) {
                         filtered.add(exercise);
                     }
                     break;
                 case "duration":
                     if (exercise.getDuration() >= threshold) {
                         filtered.add(exercise);
                     }
                     break;
             }
         }
         return filtered;
     }

     /**
     * Sets custom rest times for specific exercises.
     * 
     * @param restTimes a map of exercise names to rest times in seconds
     */
     public void setCustomRestTimes(Map<String, Integer> restTimes) {
         this.customRestTimes.putAll(restTimes);
     }

    
    /**
     * Gets the details of a specific exercise by name.
     * 
     * @param name the name of the exercise to find
     * @return the exercise if found, null otherwise
     */
    public Exercise getExerciseDetails(String name) {
        for (Exercise exercise : exercises) {
            if (exercise.getName().equalsIgnoreCase(name)) {
                return exercise;
            }
        }
        return null;
    }

    /**
     * Updates the details of an existing exercise.
     * 
     * @param name the name of the exercise to update
     * @param sets the new number of sets
     * @param reps the new number of reps
     * @param duration the new duration
     * @return true if the exercise was found and updated, false otherwise
     */
    public boolean updateExercise(String name, int sets, int reps, double duration) {
        for (Exercise exercise : exercises) {
            if (exercise.getName().equalsIgnoreCase(name)) {
                exercise.setSets(sets);
                exercise.setReps(reps);
                exercise.setDuration(duration);
                return true;
            }
        }
        return false;
    }

     // Getters and setters for the Workout class
     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Map<String, Integer> getCustomRestTimes() {
        return new HashMap<>(customRestTimes); // Return a copy to prevent modification
    }
}
