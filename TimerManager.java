import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages timing functionality for exercises and rest periods.
 * This class provides methods to track exercise durations and 
 * manage rest timers between exercises and sets.
 *
 * @author  Jadon Chanthavong
 * @version March 27, 2025
 */

public class TimerManager {
    private int workoutDuration;
    private int restDuration;
    private boolean isRunning;
    private Timer timer;
    private TimerTask currentTask;
    private long startTime;
    private long pausedTime;

    /**
     * Creates a new TimerManager.
     */
    public TimerManager() {
        this.workoutDuration = 0;
        this.restDuration = 0;
        this.isRunning = false;
        this.timer = new Timer();
    }
    
    /**
     * Tracks the duration of an exercise.
     * When the time is up, a message is displayed.
     *
     * @param duration the duration to track in seconds
     */
    public void trackDuration(int duration) {
        if (isRunning) {
            System.out.println("Timer is already running. Stop it first.");
            return;
        }
        
        this.workoutDuration = duration;
        this.isRunning = true;
        this.startTime = System.currentTimeMillis();
        
        System.out.println("Exercise timer started for " + duration + " seconds.");
        
        currentTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("\nExercise time completed!");
                isRunning = false;
            }
        };
        
        // Schedule the task to run after the specified duration
        timer.schedule(currentTask, duration * 1000);
    }
    
    /**
     * Starts a rest timer between exercises or sets.
     * When the rest period is over, a message is displayed.
     *
     * @param restDuration the rest duration in seconds
     */
    public void startRestTimer(int restDuration) {
        if (isRunning) {
            System.out.println("Timer is already running. Stop it first.");
            return;
        }
        
        this.restDuration = restDuration;
        this.isRunning = true;
        this.startTime = System.currentTimeMillis();
        
        System.out.println("Rest timer started for " + restDuration + " seconds.");
        
        // Create countdown display task to run every second
        TimerTask countdownTask = new TimerTask() {
            private int secondsLeft = restDuration;
            
            @Override
            public void run() {
                if (secondsLeft > 0) {
                    secondsLeft--;
                    if (secondsLeft % 5 == 0 || secondsLeft <= 3) { // Show every 5 seconds and last 3
                        System.out.println("Rest time remaining: " + secondsLeft + " seconds");
                    }
                } else {
                    this.cancel();
                }
            }
        };
        
        // Create completion task
        currentTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("\nRest time completed! Continue with your workout.");
                isRunning = false;
            }
        };
        
        // Schedule the countdown to run every second
        timer.scheduleAtFixedRate(countdownTask, 0, 1000);
        
        // Schedule the completion task
        timer.schedule(currentTask, restDuration * 1000);
    }
    
    /**
     * Pauses the current timer.
     */
    public void pauseTimer() {
        if (!isRunning) {
            System.out.println("No timer is currently running.");
            return;
        }
        
        pausedTime = System.currentTimeMillis() - startTime;
        if (currentTask != null) {
            currentTask.cancel();
            timer.purge();
        }
        
        System.out.println("Timer paused at " + (pausedTime / 1000) + " seconds.");
        isRunning = false;
    }
    
    /**
     * Resumes a paused timer.
     */
    public void resumeTimer() {
        if (isRunning) {
            System.out.println("Timer is already running.");
            return;
        }
        
        // Calculate remaining time
        int remainingTime;
        if (workoutDuration > 0) {
            remainingTime = workoutDuration - (int)(pausedTime / 1000);
            System.out.println("Resuming exercise timer with " + remainingTime + " seconds remaining.");
            trackDuration(remainingTime);
        } else if (restDuration > 0) {
            remainingTime = restDuration - (int)(pausedTime / 1000);
            System.out.println("Resuming rest timer with " + remainingTime + " seconds remaining.");
            startRestTimer(remainingTime);
        } else {
            System.out.println("No timer to resume.");
        }
    }
    
    /**
     * Stops the current timer.
     */
    public void stopTimer() {
        if (!isRunning && currentTask == null) {
            System.out.println("No timer is running.");
            return;
        }
        
        if (currentTask != null) {
            currentTask.cancel();
            timer.purge();
            currentTask = null;
        }
        
        isRunning = false;
        workoutDuration = 0;
        restDuration = 0;
        System.out.println("Timer stopped.");
    }
    
    /**
     * Checks if a timer is currently running.
     *
     * @return true if a timer is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Cleans up resources when the timer manager is no longer needed.
     * Should be called when the application closes.
     */
    public void shutdown() {
        if (timer != null) {
            timer.cancel();
        }
    }
}