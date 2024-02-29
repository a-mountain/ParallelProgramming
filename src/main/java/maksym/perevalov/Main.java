package maksym.perevalov;

import static java.lang.StringTemplate.STR;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import lombok.SneakyThrows;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        var start = Instant.now();
        int cellsNumber = 10;
        int particlesNumber = 8;
        double transitionFactor = 0.5;
        var duration = 5;

        int[] cells = new int[cellsNumber];
        cells[0] = particlesNumber;
        var transitions = new AtomicLong(0);

        try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().name("compute-").factory())) {
            for (int i = 0; i < particlesNumber; i++) {
                executor.execute(() -> {
                    int currentIndex = 0;
                    while (!Thread.interrupted()) {
                        var nextIndex = generateNextPosition(currentIndex, transitionFactor, cells.length);
                        synchronized (cells) {
                            cells[currentIndex] = cells[currentIndex] - 1;
                            cells[nextIndex] = cells[nextIndex] + 1;
                            currentIndex = nextIndex;
                        }
                        transitions.incrementAndGet();
                    }
                });
            }
            executor.awaitTermination(duration, TimeUnit.SECONDS);
            executor.shutdownNow();
        }
        var end = Instant.now();
        var particles = Arrays.stream(cells).reduce(Integer::sum).getAsInt();
        System.out.println(STR."Duration: \{Duration.between(start, end)}");
        System.out.println(STR."[\{duration}s] Transitions: \{transitions.get()},  Particles: \{particles}, Cells: \{cellsNumber}-\{Arrays.toString(cells)}");
    }

    private static int generateNextPosition(int currentIndex, double transitionFactor, int length) {
        double transitionValue = Math.random();
        int newIndex = transitionValue >  transitionFactor ? currentIndex + 1 : currentIndex - 1;
        boolean outOfBound = newIndex < 0 || newIndex >= length;
        return outOfBound ? currentIndex : newIndex;
    }
}
