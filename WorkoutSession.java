import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles an active workout in progress.
 * This class manages the execution of a workout, tracking progress through
 * exercises, managing timers, and providing feedback to the user.
 *
 * @author  Jadon Chanthavong
 * @version May 02, 2025
 */
public class WorkoutSession {
    private Workout workout;
    private Date startTime;
    private Date endTime;
    private Map<Exercise, ExerciseProgress> progress;
    private TimerManager timerManager;
    private boolean isActive;
    private int currentExerciseIndex;
    
    /**
     * Creates a new workout session for the specified workout.
     *
     * @param workout the workout to perform
     */
    public WorkoutSession(Workout workout) {
        this.workout = workout;
        this.progress = new HashMap<>();
        this.timerManager = new TimerManager();
        this.isActive = false;
        this.currentExerciseIndex = 0;
        
        // Initialize progress tracking for all exercises
        for (Exercise exercise : workout.listExercises()) {
            progress.put(exercise, new ExerciseProgress(exercise));
        }
    }
    
    /**
     * Starts the workout session, guiding the user through each exercise.
     *
     * @param scanner the Scanner for user input
     */
    public void startWorkout(Scanner scanner) {
        if (isActive) {
            System.out.println("Workout is already in progress.");
            return;
        }
        
        isActive = true;
        startTime = new Date();
        List<Exercise> exercises = workout.listExercises();
        
        if (exercises.isEmpty()) {
            System.out.println("Cannot start workout: No exercises in the workout plan.");
            isActive = false;
            return;
        }
        
        System.out.println("\n===== STARTING WORKOUT: " + workout.getName() + " =====");
        System.out.println("Total exercises: " + exercises.size());
        System.out.println("Estimated duration: " + workout.calculateTotalWorkoutTime() + " minutes");
        System.out.println("Starting at: " + startTime);
        System.out.println("\nPress Enter to continue to the first exercise...");
        scanner.nextLine();
        
        // Guide user through each exercise
        while (currentExerciseIndex < exercises.size() && isActive) {
            Exercise currentExercise = exercises.get(currentExerciseIndex);
            handleExercise(currentExercise, scanner);
            
            // Check if we should continue to the next exercise
            if (isActive && currentExerciseIndex < exercises.size() - 1) {
                System.out.println("\nReady for the next exercise? (Press Enter to continue or type 'pause' to pause workout)");
                String input = scanner.nextLine().trim().toLowerCase();
                
                if (input.equals("pause")) {
                    pauseWorkout();
                    System.out.println("Workout paused. Type 'resume' to continue or 'quit' to end the workout:");
                    input = scanner.nextLine().trim().toLowerCase();
                    
                    if (input.equals("resume")) {
                        resumeWorkout();
                    } else if (input.equals("quit")) {
                        finishWorkout();
                        return;
                    }
                }
                
                currentExerciseIndex++;
            } else {
                // Last exercise completed
                currentExerciseIndex++;
            }
        }
        
        // Workout completed
        if (isActive) {
            finishWorkout();
        }
    }
    
    /**
     * Handles a single exercise, guiding the user through sets and tracking progress.
     *
     * @param exercise the exercise to perform
     * @param scanner the Scanner for user input
     */
    private void handleExercise(Exercise exercise, Scanner scanner) {
        ExerciseProgress exerciseProgress = progress.get(exercise);
        
        System.out.println("\n----- EXERCISE: " + exercise.getName() + " -----");
        System.out.println("Sets: " + exercise.getSets() + " | Reps: " + exercise.getReps() + 
                         " | Duration: " + exercise.getDuration() + " seconds per set");
        
        // Loop through each set
        int currentSet = exerciseProgress.getCompletedSets() + 1;
        while (currentSet <= exercise.getSets() && isActive) {
            System.out.println("\nSet " + currentSet + " of " + exercise.getSets());
            
            // Start timer if duration is specified
            if (exercise.getDuration() > 0) {
                System.out.println("Starting timer for " + exercise.getDuration() + " seconds.");
                timerManager.trackDuration((int)exercise.getDuration());
                
                System.out.println("Timer started. Press Enter when you've completed this set...");
                scanner.nextLine();
                
                // Stop the timer if it's still running
                if (timerManager.isRunning()) {
                    timerManager.stopTimer();
                }
            } else {
                // For exercises without duration, just track reps
                System.out.println("Complete " + exercise.getReps() + " reps and press Enter when done...");
                scanner.nextLine();
            }
            
            // Update progress
            exerciseProgress.updateProgress(currentSet, exercise.getReps());
            System.out.println("Set " + currentSet + " completed!");
            
            // Rest period between sets (but not after the last set)
            if (currentSet < exercise.getSets()) {
                // Get custom rest time or use default
                int restTime = workout.getCustomRestTimes().getOrDefault(exercise.getName(), 60);
                System.out.println("Rest for " + restTime + " seconds before the next set.");
                
                timerManager.startRestTimer(restTime);
                System.out.println("Press Enter when you're ready to continue...");
                scanner.nextLine();
                
                // Stop the rest timer if it's still running
                if (timerManager.isRunning()) {
                    timerManager.stopTimer();
                }
                
                currentSet++;
            } else {
                // Last set completed
                System.out.println(exercise.getName() + " completed!");
                break;
            }
        }
        
        // Mark exercise as completed if all sets were done
        if (currentSet > exercise.getSets()) {
            exerciseProgress.markCompleted();
        }
    }
    
    /**
     * Pauses the workout session.
     */
    public void pauseWorkout() {
        if (!isActive) {
            System.out.println("No workout is currently active.");
            return;
        }
        
        isActive = false;
        if (timerManager.isRunning()) {
            timerManager.pauseTimer();
        }
        System.out.println("Workout paused.");
    }
    
    /**
     * Resumes a paused workout session.
     */
    public void resumeWorkout() {
        if (isActive) {
            System.out.println("Workout is already active.");
            return;
        }
        
        isActive = true;
        System.out.println("Workout resumed from exercise " + 
                         (currentExerciseIndex + 1) + "/" + workout.listExercises().size());
    }
    
    /**
     * Finishes the workout session and displays a summary.
     */
    public void finishWorkout() {
        if (!isActive && endTime != null) {
            System.out.println("Workout has already been finished.");
            return;
        }
        
        isActive = false;
        endTime = new Date();
        
        if (timerManager.isRunning()) {
            timerManager.stopTimer();
        }
        
        displayWorkoutSummary();
        logWorkout();
        
        // Clean up timer resources
        timerManager.shutdown();
    }
    
    /**
     * Updates progress for a specific exercise.
     *
     * @param exercise the exercise to update
     * @param completedSets the number of completed sets
     * @param completedReps the number of completed reps in the current set
     */
    public void trackProgress(Exercise exercise, int completedSets, int completedReps) {
        ExerciseProgress exerciseProgress = progress.get(exercise);
        if (exerciseProgress != null) {
            exerciseProgress.updateProgress(completedSets, completedReps);
        }
    }
    
    /**
     * Displays a summary of the completed workout.
     */
    public void displayWorkoutSummary() {
        System.out.println("\n===== WORKOUT SUMMARY =====");
        System.out.println("Workout: " + workout.getName());
        
        long durationMillis = (endTime != null ? endTime.getTime() : new Date().getTime()) - startTime.getTime();
        long durationMinutes = durationMillis / (60 * 1000);
        long durationSeconds = (durationMillis / 1000) % 60;
        
        System.out.println("Total duration: " + durationMinutes + " minutes, " + durationSeconds + " seconds");
        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + (endTime != null ? endTime : "In progress"));
        
        // Exercise completion stats
        int totalExercises = progress.size();
        int completedExercises = 0;
        
        System.out.println("\nExercise completion:");
        for (Map.Entry<Exercise, ExerciseProgress> entry : progress.entrySet()) {
            Exercise exercise = entry.getKey();
            ExerciseProgress exerciseProgress = entry.getValue();
            
            if (exerciseProgress.isCompleted()) {
                completedExercises++;
            }
            
            System.out.printf("- %s: %d/%d sets completed (%.1f%%)\n", 
                           exercise.getName(), 
                           exerciseProgress.getCompletedSets(),
                           exercise.getSets(),
                           exerciseProgress.getCompletionPercentage());
        }
        
        // Overall completion rate
        double completionRate = (double) completedExercises / totalExercises * 100;
        System.out.printf("\nOverall completion: %d/%d exercises (%.1f%%)\n", 
                       completedExercises, totalExercises, completionRate);
    }
    
    /**
     * Logs the workout details for future reference.
     * In a real application, this might save to a file or database.
     */
    public void logWorkout() {
        // In this initial version, just indicate that logging would happen here
        System.out.println("\nWorkout session logged at: " + new Date());
        
        // For future implementation:
        // - Save workout statistics to a file
        // - Update user progress history
        // - Calculate and store improvement metrics
    }
    
    /**
     * Gets the current workout being performed.
     *
     * @return the workout
     */
    public Workout getWorkout() {
        return workout;
    }
    
    /**
     * Gets the start time of this workout session.
     *
     * @return the start time
     */
    public Date getStartTime() {
        return startTime;
    }
    
    /**
     * Gets the end time of this workout session.
     *
     * @return the end time, or null if the workout is not finished
     */
    public Date getEndTime() {
        return endTime;
    }
    
    /**
     * Checks if the workout session is currently active.
     *
     * @return true if the workout is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
}