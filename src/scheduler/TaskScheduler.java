package scheduler;

import tasks.Task;
import workers.Worker;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

public class TaskScheduler {
    protected final BlockingQueue<Task> queue;
    private final List<Worker> workers;
    private final int numWorkers;
    private boolean started = false;

    public TaskScheduler(int numWorkers) {
        this.numWorkers = numWorkers;
        this.queue = new LinkedBlockingQueue<>();
        this.workers = new ArrayList<>();
    }

    public void start() {
        for (int i = 1; i <= numWorkers; i++) {
            Worker w = new Worker(i, queue);
            workers.add(w);
            w.start();
        }
        started = true;
        System.out.println("Scheduler started with " + numWorkers + " workers");
    }

    public void submit(Task task) {
        if (!started) throw new IllegalStateException("Scheduler not started");
        queue.offer(task);
        System.out.println("Submitted: " + task);
    }

    public void shutdown() throws InterruptedException {
        while (!queue.isEmpty())
            Thread.sleep(100);
        Thread.sleep(200);
        for (Worker w : workers) w.stop();
        System.out.println("Scheduler shutdown complete");
    }

    public void printStats() {
        System.out.println("\nWorker Stats:");
        for (Worker w : workers)
            System.out.printf("  Worker-%d: %d tasks completed%n",
                    w.getId(), w.getTasksCompleted());
    }

    public int getQueueSize() { return queue.size(); }
    public int getNumWorkers() { return numWorkers; }
    public List<Worker> getWorkers() { return workers; }
}