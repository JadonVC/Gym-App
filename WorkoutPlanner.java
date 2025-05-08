import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The main controller class for the Workout Planner application.
 * This class manages the overall application flow, handles user interaction,
 * and coordinates between different components of the system.
 *
 * @author  Jadon Chanthavong
 * @version May 06, 2025
 */
public class WorkoutPlanner {
    private List<Workout> savedWorkouts;
    private Workout currentWorkout;
    
    /**
     * Creates a new WorkoutPlanner instance.
     */
    public WorkoutPlanner() {
        this.savedWorkouts = new ArrayList<>();
        // Initialize with no current workout
        this.currentWorkout = null;
    }
    
    /**
     * Saves the current workout to the list of saved workouts.
     *
     * @param workoutName the name to save the workout under
     * @return true if the workout was saved successfully, false otherwise
     */
    public boolean saveWorkout(String workoutName) {
        if (currentWorkout == null) {
            System.out.println("No current workout to save.");
            return false;
        }
        
        // Check for duplicate names
        for (Workout workout : savedWorkouts) {
            if (workout.getName().equalsIgnoreCase(workoutName)) {
                System.out.println("A workout with this name already exists.");
                return false;
            }
        }
        
        // Set the name and save the workout
        currentWorkout.setName(workoutName);
        savedWorkouts.add(currentWorkout);
        System.out.println("Workout saved as: " + workoutName);
        return true;
    }
    
    /**
     * Loads a saved workout and sets it as the current workout.
     *
     * @param workoutName the name of the workout to load
     * @return true if the workout was loaded successfully, false otherwise
     */
    public boolean loadWorkout(String workoutName) {
        for (Workout workout : savedWorkouts) {
            if (workout.getName().equalsIgnoreCase(workoutName)) {
                currentWorkout = workout;
                System.out.println("Loaded workout: " + workoutName);
                return true;
            }
        }
        
        System.out.println("Workout not found: " + workoutName);
        return false;
    }
    
    /**
     * Clears the current workout plan.
     */
    public void clearWorkoutPlan() {
        if (currentWorkout != null) {
            System.out.println("Clearing current workout: " + currentWorkout.getName());
            currentWorkout = null;
        } else {
            System.out.println("No current workout to clear.");
        }
    }
    
    /**
     * Returns a list of all saved workouts.
     *
     * @return the list of saved workouts
     */
    public List<Workout> listSavedWorkouts() {
        return new ArrayList<>(savedWorkouts);
    }
    
    /**
     * Displays the main menu and handles user input.
     *
     * @param scanner the Scanner for user input
     */
    private void displayMainMenu(Scanner scanner) {
        boolean running = true;
        
        while (running) {
            System.out.println("\n===== WORKOUT PLANNER MAIN MENU =====");
            System.out.println("1. Create new workout");
            System.out.println("2. Load existing workout");
            System.out.println("3. View/Edit current workout");
            System.out.println("4. Start workout");
            System.out.println("5. Save current workout");
            System.out.println("6. List all saved workouts");
            System.out.println("7. Clear current workout");
            System.out.println("8. Exit");
            
            if (currentWorkout != null) {
                System.out.println("\nCurrent workout: " + currentWorkout.getName() + 
                               " (" + currentWorkout.listExercises().size() + " exercises)");
            } else {
                System.out.println("\nNo workout currently selected.");
            }
            
            System.out.print("\nEnter your choice: ");
            int choice = 0;
            
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }
            
            switch (choice) {
                case 1:
                    createNewWorkout(scanner);
                    break;
                case 2:
                    loadExistingWorkout(scanner);
                    break;
                case 3:
                    editCurrentWorkout(scanner);
                    break;
                case 4:
                    startWorkout(scanner);
                    break;
                case 5:
                    saveCurrentWorkout(scanner);
                    break;
                case 6:
                    listWorkouts();
                    break;
                case 7:
                    clearWorkoutPlan();
                    break;
                case 8:
                    running = false;
                    System.out.println("Thank you for using Workout Planner!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    /**
     * Creates a new workout.
     *
     * @param scanner the Scanner for user input
     */
    private void createNewWorkout(Scanner scanner) {
        System.out.println("\n===== CREATE NEW WORKOUT =====");
        System.out.print("Enter workout name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Workout name cannot be empty.");
            return;
        }
        
        currentWorkout = new Workout(name);
        System.out.println("New workout created: " + name);
        
        // Ask if user wants to add exercises now
        System.out.print("Would you like to add exercises now? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            editCurrentWorkout(scanner);
        }
    }
    
    /**
     * Loads an existing workout from the saved workouts.
     *
     * @param scanner the Scanner for user input
     */
    private void loadExistingWorkout(Scanner scanner) {
        System.out.println("\n===== LOAD WORKOUT =====");
        
        if (savedWorkouts.isEmpty()) {
            System.out.println("No saved workouts found.");
            return;
        }
        
        System.out.println("Available workouts:");
        for (int i = 0; i < savedWorkouts.size(); i++) {
            System.out.println((i + 1) + ". " + savedWorkouts.get(i).getName());
        }
        
        System.out.print("\nEnter the number of the workout to load (0 to cancel): ");
        int workoutIndex = -1;
        
        try {
            workoutIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to main menu.");
            return;
        }
        
        if (workoutIndex == -1) {
            System.out.println("Operation canceled.");
            return;
        }
        
        if (workoutIndex >= 0 && workoutIndex < savedWorkouts.size()) {
            currentWorkout = savedWorkouts.get(workoutIndex);
            System.out.println("Loaded workout: " + currentWorkout.getName());
        } else {
            System.out.println("Invalid workout number.");
        }
    }
    
    /**
     * Edits the current workout by adding, removing, or modifying exercises.
     *
     * @param scanner the Scanner for user input
     */
    private void editCurrentWorkout(Scanner scanner) {
        if (currentWorkout == null) {
            System.out.println("No workout selected. Please create or load a workout first.");
            return;
        }
        
        boolean editing = true;
        
        while (editing) {
            System.out.println("\n===== EDIT WORKOUT: " + currentWorkout.getName() + " =====");
            System.out.println("1. Add exercise");
            System.out.println("2. Remove exercise");
            System.out.println("3. Update exercise");
            System.out.println("4. List all exercises");
            System.out.println("5. Set custom rest times");
            System.out.println("6. Sort exercises");
            System.out.println("7. Calculate total workout time");
            System.out.println("8. Return to main menu");
            
            System.out.print("\nEnter your choice: ");
            int choice = 0;
            
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }
            
            switch (choice) {
                case 1:
                    addExercise(scanner);
                    break;
                case 2:
                    removeExercise(scanner);
                    break;
                case 3:
                    updateExercise(scanner);
                    break;
                case 4:
                    listExercises();
                    break;
                case 5:
                    setCustomRestTimes(scanner);
                    break;
                case 6:
                    sortExercises(scanner);
                    break;
                case 7:
                    calculateTotalTime();
                    break;
                case 8:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    /**
     * Adds a new exercise to the current workout.
     *
     * @param scanner the Scanner for user input
     */
    private void addExercise(Scanner scanner) {
        System.out.println("\n===== ADD EXERCISE =====");
        
        System.out.print("Enter exercise name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Exercise name cannot be empty.");
            return;
        }
        
        // Check for duplicate names
        for (Exercise exercise : currentWorkout.listExercises()) {
            if (exercise.getName().equalsIgnoreCase(name)) {
                System.out.println("An exercise with this name already exists.");
                return;
            }
        }
        
        int sets = getIntInput(scanner, "Enter number of sets: ", 1, 100);
        int reps = getIntInput(scanner, "Enter number of reps per set: ", 1, 1000);
        double duration = getDoubleInput(scanner, "Enter duration per set in seconds (0 for no timer): ", 0, 3600);
        
        Exercise exercise = new Exercise(name, sets, reps, duration);
        currentWorkout.addExercise(exercise);
        
        System.out.println("Exercise added: " + name);
    }
    
    /**
     * Removes an exercise from the current workout.
     *
     * @param scanner the Scanner for user input
     */
    private void removeExercise(Scanner scanner) {
        System.out.println("\n===== REMOVE EXERCISE =====");
        
        List<Exercise> exercises = currentWorkout.listExercises();
        if (exercises.isEmpty()) {
            System.out.println("No exercises in the current workout.");
            return;
        }
        
        // Display exercises
        for (int i = 0; i < exercises.size(); i++) {
            System.out.println((i + 1) + ". " + exercises.get(i).getName());
        }
        
        System.out.print("\nEnter the number of the exercise to remove (0 to cancel): ");
        int exerciseIndex = -1;
        
        try {
            exerciseIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation canceled.");
            return;
        }
        
        if (exerciseIndex == -1) {
            System.out.println("Operation canceled.");
            return;
        }
        
        if (exerciseIndex >= 0 && exerciseIndex < exercises.size()) {
            String exerciseName = exercises.get(exerciseIndex).getName();
            currentWorkout.removeExercise(exerciseName);
            System.out.println("Exercise removed: " + exerciseName);
        } else {
            System.out.println("Invalid exercise number.");
        }
    }
    
    /**
     * Updates an existing exercise in the current workout.
     *
     * @param scanner the Scanner for user input
     */
    private void updateExercise(Scanner scanner) {
        System.out.println("\n===== UPDATE EXERCISE =====");
        
        List<Exercise> exercises = currentWorkout.listExercises();
        if (exercises.isEmpty()) {
            System.out.println("No exercises in the current workout.");
            return;
        }
        
        // Display exercises
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            System.out.printf("%d. %s (Sets: %d, Reps: %d, Duration: %.1f sec)\n", 
                           i + 1, exercise.getName(), exercise.getSets(), 
                           exercise.getReps(), exercise.getDuration());
        }
        
        System.out.print("\nEnter the number of the exercise to update (0 to cancel): ");
        int exerciseIndex = -1;
        
        try {
            exerciseIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation canceled.");
            return;
        }
        
        if (exerciseIndex == -1) {
            System.out.println("Operation canceled.");
            return;
        }
        
        if (exerciseIndex >= 0 && exerciseIndex < exercises.size()) {
            Exercise selectedExercise = exercises.get(exerciseIndex);
            String exerciseName = selectedExercise.getName();
            
            System.out.println("Updating exercise: " + exerciseName);
            System.out.println("Leave any field blank to keep the current value.");
            
            // Get new values with current values as defaults
            int sets = getIntInputWithDefault(scanner, 
                                          "Enter number of sets [" + selectedExercise.getSets() + "]: ", 
                                          selectedExercise.getSets(), 1, 100);
            
            int reps = getIntInputWithDefault(scanner, 
                                          "Enter number of reps [" + selectedExercise.getReps() + "]: ", 
                                          selectedExercise.getReps(), 1, 1000);
            
            double duration = getDoubleInputWithDefault(scanner, 
                                                     "Enter duration in seconds [" + selectedExercise.getDuration() + "]: ", 
                                                     selectedExercise.getDuration(), 0, 3600);
            
            currentWorkout.updateExercise(exerciseName, sets, reps, duration);
            System.out.println("Exercise updated: " + exerciseName);
        } else {
            System.out.println("Invalid exercise number.");
        }
    }
    
    /**
     * Lists all exercises in the current workout.
     */
    private void listExercises() {
        System.out.println("\n===== EXERCISES IN " + currentWorkout.getName().toUpperCase() + " =====");
        
        List<Exercise> exercises = currentWorkout.listExercises();
        if (exercises.isEmpty()) {
            System.out.println("No exercises in the current workout.");
            return;
        }
        
        System.out.printf("%-20s %-10s %-10s %-15s\n", "NAME", "SETS", "REPS", "DURATION (sec)");
        System.out.println("---------------------------------------------------");
        
        for (Exercise exercise : exercises) {
            System.out.printf("%-20s %-10d %-10d %-15.1f\n", 
                           exercise.getName(), 
                           exercise.getSets(), 
                           exercise.getReps(), 
                           exercise.getDuration());
        }
    }
    
    /**
     * Sets custom rest times for exercises in the current workout.
     *
     * @param scanner the Scanner for user input
     */
    private void setCustomRestTimes(Scanner scanner) {
        System.out.println("\n===== SET CUSTOM REST TIMES =====");
        
        List<Exercise> exercises = currentWorkout.listExercises();
        if (exercises.isEmpty()) {
            System.out.println("No exercises in the current workout.");
            return;
        }
        
        System.out.println("Current rest times:");
        for (int i = 0; i < exercises.size(); i++) {
            String exerciseName = exercises.get(i).getName();
            int restTime = currentWorkout.getCustomRestTimes().getOrDefault(exerciseName, 60);
            System.out.printf("%d. %s: %d seconds\n", i + 1, exerciseName, restTime);
        }
        
        System.out.print("\nEnter the number of the exercise to set rest time (0 to cancel): ");
        int exerciseIndex = -1;
        
        try {
            exerciseIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation canceled.");
            return;
        }
        
        if (exerciseIndex == -1) {
            System.out.println("Operation canceled.");
            return;
        }
        
        if (exerciseIndex >= 0 && exerciseIndex < exercises.size()) {
            String exerciseName = exercises.get(exerciseIndex).getName();
            int currentRestTime = currentWorkout.getCustomRestTimes().getOrDefault(exerciseName, 60);
            
            int restTime = getIntInputWithDefault(scanner, 
                                               "Enter rest time in seconds [" + currentRestTime + "]: ", 
                                               currentRestTime, 0, 300);
            
            // Create a temporary map to set the individual rest time
            java.util.Map<String, Integer> restTimeMap = new java.util.HashMap<>();
            restTimeMap.put(exerciseName, restTime);
            currentWorkout.setCustomRestTimes(restTimeMap);
            
            System.out.println("Rest time for " + exerciseName + " set to " + restTime + " seconds.");
        } else {
            System.out.println("Invalid exercise number.");
        }
    }
    
    /**
     * Sorts the exercises in the current workout.
     *
     * @param scanner the Scanner for user input
     */
    private void sortExercises(Scanner scanner) {
        System.out.println("\n===== SORT EXERCISES =====");
        
        if (currentWorkout.listExercises().isEmpty()) {
            System.out.println("No exercises to sort.");
            return;
        }
        
        System.out.println("Sort exercises by:");
        System.out.println("1. Name");
        System.out.println("2. Sets");
        System.out.println("3. Reps");
        System.out.println("4. Duration");
        
        System.out.print("\nEnter your choice: ");
        int choice = 0;
        
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Operation canceled.");
            return;
        }
        
        String sortBy;
        switch (choice) {
            case 1:
                sortBy = "name";
                break;
            case 2:
                sortBy = "sets";
                break;
            case 3:
                sortBy = "reps";
                break;
            case 4:
                sortBy = "duration";
                break;
            default:
                System.out.println("Invalid option. Operation canceled.");
                return;
        }
        
        currentWorkout.sortExercises(sortBy);
        System.out.println("Exercises sorted by " + sortBy + ".");
        
        // Show the sorted list
        listExercises();
    }
    
    /**
     * Calculates and displays the total estimated workout time.
     */
    private void calculateTotalTime() {
        System.out.println("\n===== TOTAL WORKOUT TIME =====");
        
        if (currentWorkout.listExercises().isEmpty()) {
            System.out.println("No exercises in the workout.");
            return;
        }
        
        double totalTimeMinutes = currentWorkout.calculateTotalWorkoutTime();
        int hours = (int) (totalTimeMinutes / 60);
        int minutes = (int) (totalTimeMinutes % 60);
        int seconds = (int) ((totalTimeMinutes * 60) % 60);
        
        System.out.println("Estimated total time for " + currentWorkout.getName() + ":");
        System.out.printf("%d hours, %d minutes, %d seconds\n", hours, minutes, seconds);
    }
    
    /**
     * Starts a workout session with the current workout.
     *
     * @param scanner the Scanner for user input
     */
    private void startWorkout(Scanner scanner) {
        if (currentWorkout == null) {
            System.out.println("No workout selected. Please create or load a workout first.");
            return;
        }
        
        List<Exercise> exercises = currentWorkout.listExercises();
        if (exercises.isEmpty()) {
            System.out.println("Cannot start workout: No exercises in the workout plan.");
            return;
        }
        
        System.out.println("\n===== START WORKOUT =====");
        
        // Display workout summary before starting
        System.out.println("Workout: " + currentWorkout.getName());
        System.out.println("Exercises: " + exercises.size());
        double totalTimeMinutes = currentWorkout.calculateTotalWorkoutTime();
        System.out.printf("Estimated duration: %.1f minutes\n", totalTimeMinutes);
        
        System.out.print("\nReady to begin workout? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            // Create and start a workout session
            WorkoutSession session = new WorkoutSession(currentWorkout);
            session.startWorkout(scanner);
        } else {
            System.out.println("Workout canceled.");
        }
    }
    
    /**
     * Saves the current workout with a name provided by the user.
     *
     * @param scanner the Scanner for user input
     */
    private void saveCurrentWorkout(Scanner scanner) {
        if (currentWorkout == null) {
            System.out.println("No workout to save. Please create a workout first.");
            return;
        }
        
        System.out.println("\n===== SAVE WORKOUT =====");
        System.out.print("Enter name to save workout as [" + currentWorkout.getName() + "]: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            name = currentWorkout.getName();
        }
        
        saveWorkout(name);
    }
    
    /**
     * Lists all saved workouts.
     */
    private void listWorkouts() {
        System.out.println("\n===== SAVED WORKOUTS =====");
        
        if (savedWorkouts.isEmpty()) {
            System.out.println("No saved workouts found.");
            return;
        }
        
        for (int i = 0; i < savedWorkouts.size(); i++) {
            Workout workout = savedWorkouts.get(i);
            int exerciseCount = workout.listExercises().size();
            System.out.printf("%d. %s (%d exercises)\n", i + 1, workout.getName(), exerciseCount);
        }
    }
    
    /**
     * Helper method to get integer input with validation.
     *
     * @param scanner the Scanner for user input
     * @param prompt the prompt to display
     * @param min the minimum valid value
     * @param max the maximum valid value
     * @return the validated integer input
     */
    private int getIntInput(Scanner scanner, String prompt, int min, int max) {
        int value;
        
        while (true) {
            System.out.print(prompt);
            
            try {
                String input = scanner.nextLine().trim();
                value = Integer.parseInt(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Helper method to get integer input with a default value.
     *
     * @param scanner the Scanner for user input
     * @param prompt the prompt to display
     * @param defaultValue the default value to use if input is empty
     * @param min the minimum valid value
     * @param max the maximum valid value
     * @return the validated integer input or the default value
     */
    private int getIntInputWithDefault(Scanner scanner, String prompt, int defaultValue, int min, int max) {
        while (true) {
            System.out.print(prompt);
            
            try {
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    return defaultValue;
                }
                
                int value = Integer.parseInt(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Helper method to get double input with validation.
     *
     * @param scanner the Scanner for user input
     * @param prompt the prompt to display
     * @param min the minimum valid value
     * @param max the maximum valid value
     * @return the validated double input
     */
    private double getDoubleInput(Scanner scanner, String prompt, double min, double max) {
        double value;
        
        while (true) {
            System.out.print(prompt);
            
            try {
                String input = scanner.nextLine().trim();
                value = Double.parseDouble(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Helper method to get double input with a default value.
     *
     * @param scanner the Scanner for user input
     * @param prompt the prompt to display
     * @param defaultValue the default value to use if input is empty
     * @param min the minimum valid value
     * @param max the maximum valid value
     * @return the validated double input or the default value
     */
    private double getDoubleInputWithDefault(Scanner scanner, String prompt, double defaultValue, double min, double max) {
        while (true) {
            System.out.print(prompt);
            
            try {
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    return defaultValue;
                }
                
                double value = Double.parseDouble(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * The main entry point for the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Workout Planner!");
        System.out.println("A terminal-based application to plan and track your workouts.");
        
        Scanner scanner = new Scanner(System.in);
        WorkoutPlanner planner = new WorkoutPlanner();
        
        planner.displayMainMenu(scanner);
        
        scanner.close();
        System.out.println("Goodbye!");
    }
}