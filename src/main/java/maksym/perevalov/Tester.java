package maksym.perevalov;

import java.util.ArrayList;
import java.util.List;

import maksym.perevalov.model.LockPerCellModel;

public class Tester {
    public static void main(String[] args) {
        int particles = 8;
        double factor = 0.5;

        int duration = 5;
        int iterations = 10;

        var cells = List.of(2, 4, 8, 16, 32, 64, 128, 256, 524);
        var value = new ArrayList<Long>();
        System.out.println("LockPerCellModel");
        for (Integer cell : cells) {
            for (int i = 0; i < iterations; i++) {
                System.out.println(STR."Iteration #\{i}");
                value.add(run(cell, particles, factor, duration));
            }
        }
        System.out.println(STR."cells = \{cells}");
        System.out.println(STR."value = \{value}");
    }

    private static long run(int cellsNumber, int particlesNumber, double transitionFactor, int duration) {
        var model = new LockPerCellModel(cellsNumber, particlesNumber, transitionFactor);
        return new Runner(model, duration, 2, false).start();
    }
}
