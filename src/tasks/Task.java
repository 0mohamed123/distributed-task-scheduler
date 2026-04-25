package tasks;

import java.util.concurrent.Callable;

public class Task implements Comparable<Task> {
    private static int counter = 0;

    private final int id;
    private final String name;
    private final int priority;
    private final Callable<String> job;
    private TaskStatus status;
    private String result;
    private long createdAt;
    private long completedAt;

    public Task(String name, int priority, Callable<String> job) {
        this.id = ++counter;
        this.name = name;
        this.priority = priority;
        this.job = job;
        this.status = TaskStatus.PENDING;
        this.createdAt = System.currentTimeMillis();
    }

    public String execute() {
        this.status = TaskStatus.RUNNING;
        try {
            this.result = job.call();
            this.status = TaskStatus.COMPLETED;
        } catch (Exception e) {
            this.result = "ERROR: " + e.getMessage();
            this.status = TaskStatus.FAILED;
        }
        this.completedAt = System.currentTimeMillis();
        return this.result;
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPriority() { return priority; }
    public TaskStatus getStatus() { return status; }
    public String getResult() { return result; }
    public long getCreatedAt() { return createdAt; }
    public long getCompletedAt() { return completedAt; }
    public long getDuration() { return completedAt - createdAt; }

    @Override
    public String toString() {
        return String.format("Task[id=%d, name=%s, priority=%d, status=%s]",
                id, name, priority, status);
    }
}