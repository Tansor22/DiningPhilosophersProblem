package philosophers;

public class Plato extends Philosopher {
    @Override
    protected int[] forks() {
        return new int[] {2, 3};
    }

    @Override
    protected String name() {
        return "Plato";
    }
}
