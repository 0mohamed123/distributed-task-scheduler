import scheduler.TaskScheduler;
import tasks.Task;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Distributed Task Scheduler Demo ===\n");

        TaskScheduler scheduler = new TaskScheduler(3);
        scheduler.start();

        System.out.println("\nSubmitting tasks...");

        scheduler.submit(new Task("ML Training", 10, () -> {
            Thread.sleep(100);
            return "Model trained: accuracy=0.95";
        }));

        scheduler.submit(new Task("Data Preprocessing", 8, () -> {
            Thread.sleep(50);
            return "Processed 10000 samples";
        }));

        scheduler.submit(new Task("Model Evaluation", 7, () -> {
            Thread.sleep(80);
            return "F1-score=0.92";
        }));

        scheduler.submit(new Task("Log Metrics", 5, () -> {
            Thread.sleep(30);
            return "Metrics logged";
        }));

        scheduler.submit(new Task("Save Checkpoint", 9, () -> {
            Thread.sleep(60);
            return "Checkpoint saved";
        }));

        scheduler.shutdown();
        scheduler.printStats();

        System.out.println("\nDone!");
    }
}