package philosophers;

public class Heraclitus extends Philosopher {
    @Override
    protected int[] forks() {
        return new int[] {4, 0};
    }

    @Override
    protected String name() {
        return "Heraclitus";
    }

    @Override
    protected int limit() {
        return 3;
    }
}
