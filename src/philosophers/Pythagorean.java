package philosophers;

public class Pythagorean extends Philosopher {
    @Override
    protected int[] forks() {
        return new int[] {4,5};
    }

    @Override
    protected String name() {
        return "Pythagorean";
    }
}
