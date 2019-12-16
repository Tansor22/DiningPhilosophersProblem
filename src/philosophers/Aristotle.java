package philosophers;

public class Aristotle extends Philosopher{
    @Override
    protected int[] forks() {
        return new int[] {0, 1};
    }

    @Override
    protected String name() {
        return "Aristotle";
    }
}
