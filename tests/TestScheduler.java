import scheduler.TaskScheduler;
import tasks.Task;
import tasks.TaskStatus;

public class TestScheduler {
    static int passed = 0;
    static int failed = 0;

    static void test(boolean condition, String name) {
        if (condition) {
            System.out.println("  [PASS] " + name);
            passed++;
        } else {
            System.out.println("  [FAIL] " + name);
            failed++;
        }
    }

    static void testTaskCreation() {
        System.out.println("\n[Task Creation]");
        Task t = new Task("Test", 5, () -> "done");
        test(t.getName().equals("Test"), "name correct");
        test(t.getPriority() == 5, "priority correct");
        test(t.getStatus() == TaskStatus.PENDING, "initial status PENDING");
        test(t.getId() > 0, "id assigned");
    }

    static void testTaskExecution() {
        System.out.println("\n[Task Execution]");
        Task t = new Task("Compute", 5, () -> "result=42");
        String result = t.execute();
        test(result.equals("result=42"), "result correct");
        test(t.getStatus() == TaskStatus.COMPLETED, "status COMPLETED");
        test(t.getDuration() >= 0, "duration recorded");
    }

    static void testTaskFailure() {
        System.out.println("\n[Task Failure]");
        Task t = new Task("Failing", 5, () -> {
            throw new RuntimeException("intentional error");
        });
        t.execute();
        test(t.getStatus() == TaskStatus.FAILED, "status FAILED on error");
        test(t.getResult().contains("ERROR"), "error message in result");
    }

    static void testTaskPriority() {
        System.out.println("\n[Task Priority]");
        Task high = new Task("High", 10, () -> "done");
        Task low = new Task("Low", 1, () -> "done");
        test(high.compareTo(low) < 0, "high priority comes first");
        test(low.compareTo(high) > 0, "low priority comes last");
    }

    static void testScheduler() throws InterruptedException {
        System.out.println("\n[Scheduler]");
        TaskScheduler scheduler = new TaskScheduler(2);
        scheduler.start();
        test(scheduler.getNumWorkers() == 2, "worker count correct");

        scheduler.submit(new Task("T1", 5, () -> "ok"));
        scheduler.submit(new Task("T2", 5, () -> "ok"));
        scheduler.submit(new Task("T3", 5, () -> "ok"));

        scheduler.shutdown();

        int total = scheduler.getWorkers().stream()
                .mapToInt(w -> w.getTasksCompleted()).sum();
        test(total == 3, "all tasks completed");
        test(scheduler.getQueueSize() == 0, "queue empty after shutdown");
    }

    static void testSchedulerNotStarted() {
        System.out.println("\n[Error Handling]");
        TaskScheduler scheduler = new TaskScheduler(2);
        boolean threw = false;
        try {
            scheduler.submit(new Task("T", 1, () -> "ok"));
        } catch (IllegalStateException e) {
            threw = true;
        }
        test(threw, "throws if not started");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Running all tests...");
        testTaskCreation();
        testTaskExecution();
        testTaskFailure();
        testTaskPriority();
        testScheduler();
        testSchedulerNotStarted();

        System.out.println("\n==============================");
        System.out.println("  " + passed + " passed | " + failed + " failed");
        System.out.println("==============================");
    }
}