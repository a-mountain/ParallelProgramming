package maksym.perevalov.model;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class LockPerCellModel extends BaseModel {
    private final int[] cells;
    private final Object[] locks;

    public LockPerCellModel(int cellsNumber, int particlesNumber, double transitionFactor) {
        super(cellsNumber, particlesNumber, transitionFactor);
        this.cells = new int[cellsNumber];
        this.cells[0] = particlesNumber;
        this.locks = Stream.generate(Object::new).limit(cellsNumber).toArray();
    }

    @Override
    public int performTransition(int currentIndex, RandomGenerator random) {
        var nextIndex = generateNextPosition(currentIndex, random);

        synchronized (locks[currentIndex]) {
            cells[currentIndex] = cells[currentIndex] - 1;
        }

        synchronized (locks[nextIndex]) {
            cells[nextIndex] = cells[nextIndex] + 1;
        }

        return nextIndex;
    }

    @Override
    public int[] readCells() {
        return Arrays.copyOf(cells, cells.length);
    }
}
