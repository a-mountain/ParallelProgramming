package maksym.perevalov.model;

import java.util.random.RandomGenerator;

public abstract class BaseModel {
    public final int cellsNumber;
    public final int particlesNumber;
    public final double transitionFactor;

    public BaseModel(int cellsNumber, int particlesNumber, double transitionFactor) {
        this.cellsNumber = cellsNumber;
        this.particlesNumber = particlesNumber;
        this.transitionFactor = transitionFactor;
    }

    public abstract int performTransition(int currentIndex, RandomGenerator random);

    public abstract int[] readCells();

    protected int generateNextPosition(int currentIndex, RandomGenerator random) {
        double transitionValue = random.nextDouble();
        int newIndex = transitionValue > transitionFactor ? currentIndex + 1 : currentIndex - 1;
        boolean outOfBound = newIndex < 0 || newIndex >= cellsNumber;
        return outOfBound ? currentIndex : newIndex;
    }
}
