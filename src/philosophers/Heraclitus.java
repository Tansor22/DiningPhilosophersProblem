package philosophers;

public class Heraclitus extends Philosopher {
    @Override
    protected int[] forks() {
        return new int[] {5, 1};
    }

    @Override
    protected String name() {
        return "Heraclitus";
    }
}
