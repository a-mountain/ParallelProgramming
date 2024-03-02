package maksym.perevalov;

import maksym.perevalov.model.BaseModel;
import maksym.perevalov.model.ReentrantLockPerCellModel;

public class Main {
    public static void main(String[] args) {
        int cells = 10;
        int particles = 8;
        double factor = 0.5;

        int period = 5;
        int duration = 60;

        var model = new ReentrantLockPerCellModel(cells, particles, factor);
        run(factor, duration, period, model);
    }

    private static void run(double factor, int duration, int period, BaseModel model) {
        var name = model.getClass().getSimpleName();
        System.out.println(STR."Running model \{name}, factor = \{factor}, duration = \{duration}s");
        new Runner(model, duration, period, true).start();
    }
}
