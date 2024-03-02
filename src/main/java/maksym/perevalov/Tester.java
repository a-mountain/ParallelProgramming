package maksym.perevalov;

import maksym.perevalov.model.GlobalLockModel;

public class Tester {
    // R = 53010057
    // S = 47806916, 36727538
    public static void main(String[] args) {
        int cells = 10;
        int particles = 8;
        double factor = 0.5;

        int duration = 10;
        int iterations = 15;

        System.out.println(STR."LockPerCellModel - Cells: \{cells}, Particles: \{particles}, TransitionFactor: \{factor}, Duration: \{duration}");
        long total = 0;
        for (int i = 0; i < iterations; i++) {
            System.out.println(STR."Iteration #\{i}");
            total += run(cells, particles, factor, duration);
        }
        System.out.println(STR."LockPerCellModel - Cells: \{cells}, Particles: \{particles}, TransitionFactor: \{factor}, Duration: \{duration} = Average transitions: \{total / iterations}");
    }

    private static long run(int cellsNumber, int particlesNumber, double transitionFactor, int duration) {
        var model = new GlobalLockModel(cellsNumber, particlesNumber, transitionFactor);
        return new Runner(model, duration, 2, true).start();
    }
}
