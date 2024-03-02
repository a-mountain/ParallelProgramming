package maksym.perevalov.model;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.random.RandomGenerator;

public class NoLockModel extends BaseModel {
    private final int[] cells;

    public NoLockModel(int cellsNumber, int particlesNumber, double transitionFactor) {
        super(cellsNumber, particlesNumber, transitionFactor);
        this.cells = new int[cellsNumber];
        this.cells[0] = particlesNumber;
    }

    @Override
    public int performTransition(int currentIndex, RandomGenerator random) {
        var nextIndex = generateNextPosition(currentIndex, random);

        cells[currentIndex] = cells[currentIndex] - 1;
        cells[nextIndex] = cells[nextIndex] + 1;

        return nextIndex;
    }

    @Override
    public int[] readCells() {
        return Arrays.copyOf(cells, cells.length);
    }
}
