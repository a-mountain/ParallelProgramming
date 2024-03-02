package maksym.perevalov;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import maksym.perevalov.model.BaseModel;

public class Runner {
    private final BaseModel model;
    private final int duration;
    private final long[] transitions;
    private final int period;
    private final boolean showProgress;

    private volatile CountDownLatch suspendLatch;
    private volatile boolean running = true;
    private volatile boolean suspended = false;

    public Runner(BaseModel model, int duration, int period, boolean showProgress) {
        this.model = model;
        this.duration = duration;
        this.transitions = new long[model.particlesNumber];
        this.period = period;
        this.showProgress = showProgress;
    }

    @SneakyThrows
    public long start() {
        try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().name("compute-", 0).factory())) {
            printState(false, 0);
            for (int i = 0; i < model.particlesNumber; i++) {
                final int finalI = i;
                executor.execute(() -> run(finalI));
            }
            if (showProgress) executor.execute(this::progressChecker);
            executor.awaitTermination(duration, TimeUnit.SECONDS);
            running = false;
        }
        printState(false, duration);
        return countTransitions();
    }

    @SneakyThrows
    private void progressChecker() {
        int iterations = duration / period;
        for (int i = 1; i < iterations; i++) {
            Thread.sleep(period * 1000);
            printState(true, i * period);
        }
    }

    private void run(int index) {
        int currentIndex = 0;
        var random = ThreadLocalRandom.current();

        while (running) {
            currentIndex = model.performTransition(currentIndex, random);
            transitions[index] = transitions[index] + 1;
            suspendPoint();
        }
    }

    @SneakyThrows
    private void suspendPoint() {
        if (suspended && suspendLatch != null) {
            suspendLatch.countDown();
            if (suspendLatch != null) suspendLatch.await();
            while (suspended) Thread.onSpinWait();
        }
    }

    @SneakyThrows
    private Snapshot takeSnapshot() {
        suspendLatch = new CountDownLatch(model.particlesNumber);
        suspended = true;

        suspendLatch.await();
        var cellsCopy = model.readCells();
        var transitions = countTransitions();

        suspendLatch = null;
        suspended = false;

        return new Snapshot(cellsCopy, transitions);
    }

    private long countTransitions() {
        return Arrays.stream(transitions).reduce(Long::sum).getAsLong();
    }

    private void printState(boolean suspended, int seconds) {
        int[] cellsCopy;
        long transitions;

        if (suspended) {
            var snapshot = takeSnapshot();
            cellsCopy = snapshot.cells;
            transitions = snapshot.transitions;
        } else {
            cellsCopy = model.readCells();
            transitions = countTransitions();
        }


        var particles = Arrays.stream(cellsCopy).reduce(Integer::sum).getAsInt();
        var crystal = Arrays.toString(cellsCopy);
        System.out.println(STR."[\{seconds}s] Transitions: \{transitions},  Particles: \{particles}, Cells: \{cellsCopy.length}-\{crystal}");
    }

    @RequiredArgsConstructor
    private static class Snapshot {
        public final int[] cells;
        public final long transitions;
    }
}
