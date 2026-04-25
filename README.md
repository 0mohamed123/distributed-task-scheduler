# Distributed Task Scheduler (Java)

![Language](https://img.shields.io/badge/Language-Java%2023-orange)
![Tests](https://img.shields.io/badge/Tests-15%20passing-green)
![Concurrency](https://img.shields.io/badge/Concurrency-MultiThreaded-blue)

Multi-threaded task scheduler with priority support, fault tolerance,
and worker pool management built using Java concurrency primitives.

## Demo Output

    Scheduler started with 3 workers
    Worker-1 executing: ML Training
    Worker-2 executing: Data Preprocessing
    Worker-3 executing: Model Evaluation
    Worker-2 completed: Data Preprocessing -> Processed 10000 samples
    Worker-1 completed: ML Training -> Model trained: accuracy=0.95
    Worker-3 completed: Model Evaluation -> F1-score=0.92

    Worker Stats:
      Worker-1: 1 tasks completed
      Worker-2: 3 tasks completed
      Worker-3: 1 tasks completed

## Quick Start

    git clone https://github.com/0mohamed123/distributed-task-scheduler.git
    cd distributed-task-scheduler/src
    javac -d . scheduler/*.java tasks/*.java workers/*.java Main.java
    java Main

    # Run tests
    cd ../tests
    javac -cp ../src -d . ../src/scheduler/*.java ../src/tasks/*.java ../src/workers/*.java TestScheduler.java
    java -cp ".;../src" TestScheduler

## Usage

    TaskScheduler scheduler = new TaskScheduler(3);
    scheduler.start();

    scheduler.submit(new Task("ML Training", 10, () -> {
        Thread.sleep(100);
        return "Model trained: accuracy=0.95";
    }));

    scheduler.shutdown();
    scheduler.printStats();

## Architecture

    TaskScheduler
        |-- Worker-1 (Thread)  --|
        |-- Worker-2 (Thread)  --|--> BlockingQueue<Task>
        |-- Worker-3 (Thread)  --|

    Task: name + priority + Callable<String>
    Worker: polls queue, executes tasks, tracks completion

## Features

- Multi-threaded worker pool
- Blocking queue for thread-safe task distribution
- Task priority with Comparable interface
- Fault tolerance - failed tasks marked with error message
- Graceful shutdown - waits for queue to drain
- Worker statistics tracking

## Test Results

    15 passed | 0 failed

    Tests cover: task creation, execution, failure handling,
    priority ordering, scheduler start/shutdown,
    all tasks completed, queue empty, error on not started

## Technologies

- Java 23
- java.util.concurrent (BlockingQueue, PriorityBlockingQueue)
- Multi-threading (Runnable, Thread)
- Callable interface for task results