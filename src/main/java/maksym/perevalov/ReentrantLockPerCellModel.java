package maksym.perevalov;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import lombok.SneakyThrows;

public class ReentrantLockPerCellModel {
    private final int particlesNumber;
    private final double transitionFactor;
    private final int duration;

    private final int[] cells;
    private final long[] transitionsCounter;
    private volatile boolean running = true;
    private final ReentrantLock[] locks;

    public ReentrantLockPerCellModel(int cellsNumber, int particlesNumber, double transitionFactor, int duration) {
        this.cells = new int[cellsNumber];
        this.cells[0] = particlesNumber;
        this.transitionsCounter = new long[particlesNumber];
        this.locks = Stream.generate((ReentrantLock::new)).limit(cellsNumber).toArray(ReentrantLock[]::new);

        this.particlesNumber = particlesNumber;
        this.transitionFactor = transitionFactor;
        this.duration = duration;
    }

    @SneakyThrows
    public long start() {
        try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().name("compute-", 0).factory())) {
            for (int i = 0; i < particlesNumber; i++) {
                final int finalI = i;
                executor.execute(() -> run(finalI));
            }
            executor.awaitTermination(duration, TimeUnit.SECONDS);
            running = false;
        }
        printState();
        return countTransitions();
    }

    private void run(int index) {
        int currentIndex = 0;
        var random = ThreadLocalRandom.current();
        long transitionCounter = 0;
        while (running) {
            var nextIndex = generateNextPosition(currentIndex, random);

            locks[currentIndex].lock();
            cells[currentIndex] = cells[currentIndex] - 1;
            locks[currentIndex].unlock();

            locks[nextIndex].lock();
            cells[nextIndex] = cells[nextIndex] + 1;
            locks[nextIndex].unlock();

            currentIndex = nextIndex;
            transitionCounter++;
        }
        this.transitionsCounter[index] = transitionCounter;
    }

    private void printState() {
        var particles = Arrays.stream(cells).reduce(Integer::sum).getAsInt();
        var crystal = Arrays.toString(cells);
        var transitions = countTransitions();
        var cellsNumber = cells.length;
        System.out.println(STR."[\{duration}s] Transitions: \{transitions},  Particles: \{particles}, Cells: \{cellsNumber}-\{crystal}");
    }

    private long countTransitions() {
        return Arrays.stream(transitionsCounter).reduce(Long::sum).getAsLong();
    }

    private int generateNextPosition(int currentIndex, RandomGenerator random) {
        double transitionValue = random.nextDouble();
        int newIndex = transitionValue >  transitionFactor ? currentIndex + 1 : currentIndex - 1;
        boolean outOfBound = newIndex < 0 || newIndex >= cells.length;
        return outOfBound ? currentIndex : newIndex;
    }
}
