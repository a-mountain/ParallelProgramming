package maksym.perevalov;

public class Tester {
    public static void main(String[] args) {
        int cells = 10;
        int particles = 8;
        double factor = 0.5;

        int duration = 5;
        int iterations = 15;

        System.out.println(STR."GlobalLockModel - Cells: \{cells}, Particles: \{particles}, TransitionFactor: \{factor}, Duration: \{duration}");
        long total = 0;
        for (int i = 0; i < iterations; i++) {
            System.out.println(STR."Iteration #\{i}");
            total += run(cells, particles, factor, duration);
        }
        System.out.println(STR."GlobalLockModel - Cells: \{cells}, Particles: \{particles}, TransitionFactor: \{factor}, Duration: \{duration} = Average transitions: \{total / iterations}");
    }

    private static long run(int cellsNumber, int particlesNumber, double transitionFactor, int duration) {
        return new GlobalLockModel(cellsNumber, particlesNumber, transitionFactor, duration).start();
    }
}
