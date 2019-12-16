package philosophers;

public class Plato extends Philosopher {
    @Override
    protected int[] forks() {
        return new int[] {3, 4};
    }

    @Override
    protected String name() {
        return "Plato";
    }
}
