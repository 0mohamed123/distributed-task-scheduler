package workers;

import tasks.Task;
import tasks.TaskStatus;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
    private final int id;
    private final BlockingQueue<Task> queue;
    private volatile boolean running = true;
    private int tasksCompleted = 0;
    private Thread thread;

    public Worker(int id, BlockingQueue<Task> queue) {
        this.id = id;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Task task = queue.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (task != null) {
                    System.out.printf("  Worker-%d executing: %s%n", id, task.getName());
                    task.execute();
                    tasksCompleted++;
                    System.out.printf("  Worker-%d completed: %s -> %s%n",
                            id, task.getName(), task.getResult());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void start() {
        thread = new Thread(this, "Worker-" + id);
        thread.start();
    }

    public void stop() {
        running = false;
        if (thread != null) thread.interrupt();
    }

    public int getId() { return id; }
    public int getTasksCompleted() { return tasksCompleted; }
    public boolean isRunning() { return running; }
}