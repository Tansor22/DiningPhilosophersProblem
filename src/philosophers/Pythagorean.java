package philosophers;

public class Pythagorean extends Philosopher {
    @Override
    protected int[] forks() {
        return new int[] {3,4};
    }

    @Override
    protected String name() {
        return "Pythagorean";
    }
}
