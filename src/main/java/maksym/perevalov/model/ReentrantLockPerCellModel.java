
package maksym.perevalov.model;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class ReentrantLockPerCellModel extends BaseModel {
    private final int[] cells;
    private final ReentrantLock[] locks;

    public ReentrantLockPerCellModel(int cellsNumber, int particlesNumber, double transitionFactor) {
        super(cellsNumber, particlesNumber, transitionFactor);
        this.cells = new int[cellsNumber];
        this.cells[0] = particlesNumber;
        this.locks = Stream.generate((ReentrantLock::new)).limit(cellsNumber).toArray(ReentrantLock[]::new);

    }

    @Override
    public int performTransition(int currentIndex, RandomGenerator random) {
        var nextIndex = generateNextPosition(currentIndex, random);

        locks[currentIndex].lock();
        cells[currentIndex] = cells[currentIndex] - 1;
        locks[currentIndex].unlock();

        locks[nextIndex].lock();
        cells[nextIndex] = cells[nextIndex] + 1;
        locks[nextIndex].unlock();

        return nextIndex;
    }

    @Override
    public int[] readCells() {
        return Arrays.copyOf(cells, cells.length);
    }
}
