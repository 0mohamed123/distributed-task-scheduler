package scheduler;

import tasks.Task;
import workers.Worker;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityScheduler extends TaskScheduler {

    public PriorityScheduler(int numWorkers) {
        super(numWorkers);
        // Override queue with priority queue
        var priorityQueue = new PriorityBlockingQueue<Task>();
        // Note: uses parent queue field for workers
    }

    public static PriorityScheduler create(int numWorkers) {
        return new PriorityScheduler(numWorkers);
    }
}