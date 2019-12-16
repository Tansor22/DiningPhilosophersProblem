package philosophers;

public class Socrates extends Philosopher{
    @Override
    protected int[] forks() {
        return new int[] {1, 2};
    }

    @Override
    protected String name() {
        return "Socrates";
    }
}
