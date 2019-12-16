package philosophers;

import java.util.Arrays;

import static utils.Utils.*;

public abstract class Philosopher {
    protected abstract int[] forks();

    protected abstract String name();

    // initialize
    protected void init() {
        Thread.currentThread().setName(name());
    }

    private void greet() {
        say("Hello! My name is " + name() + "." +
                " I will use " + Arrays.toString(forks()) + " forks.");
    }

    public static void main(String[] args) {
        Philosopher philosopher = PhilosophersFabric.of(args[0]);
        philosopher.init();
        philosopher.greet();


    }
}
