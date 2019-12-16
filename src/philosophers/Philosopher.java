package philosophers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static utils.Utils.*;

public abstract class Philosopher {
    protected abstract int[] forks();

    protected abstract String name();

    // Philosopher's name -> amount of food eaten
    protected static Map<String, Integer> EATING_RESULTS = new HashMap<>();
    // is i-fork taken
    protected static boolean[] FORKS_TAKEN = new boolean[5];

    protected int limit() { return 10;}

    // initialize
    protected void init() {
        Thread.currentThread().setName(name());
        EATING_RESULTS.put(name(), 0);
    }

    private int pretty(int num) {
        return ++num;
    }

    private int[] pretty(int[] nums) {
        for (int i = 0; i < nums.length; i++)
            nums[i]++;
        return nums;
    }

    // take 2 forks simultaneously
    protected void takeForks() {
        int[] forks = forks();

        // until first fork is not taken
        while (FORKS_TAKEN[forks[0]]) {
            sleep(pretty(forks[0]) + " fork is occupied. Waiting for it to release...", 1);
        }
        // have taken first fork
        FORKS_TAKEN[forks[0]] = true;
        // herein we can start thinking

        while (FORKS_TAKEN[forks[1]]) {
            sleep(pretty(forks[1]) + " fork is occupied. Waiting for it to release...", 1);
        }
        // have taken second fork
        FORKS_TAKEN[forks[1]] = true;
    }

    protected void releaseForks() {
        int[] forks = forks();

        // have released first fork
        FORKS_TAKEN[forks[0]] = false;
        say("I've released " + pretty(forks[0]) + " fork.");

        // have released second fork
        FORKS_TAKEN[forks[1]] = false;
        say("I've released " + pretty(forks[1]) + " fork.");
    }

    private void greet() {
        say("Hello! My name is " + name() + "." +
                " I will use " + Arrays.toString(pretty(forks())) + " forks.");
    }

    public static void main(String[] args) {
        Philosopher philosopher = PhilosophersFabric.of(args[0]);
        philosopher.init();
        philosopher.greet();
        while (EATING_RESULTS.get(philosopher.name()) < philosopher.limit()) {
            philosopher.takeForks();
            philosopher.think();
            philosopher.eat();
            philosopher.releaseForks();
            say("I'am done here! For a while...");
        }
        // Finished
        say("My totals: I've eaten " + EATING_RESULTS.get(philosopher.name()) + " amount of food!", 3);
    }

    protected void eat() {
        say("Let me eat...", 2);
        EATING_RESULTS.merge(name(), 1, Integer::sum);
    }

    protected void think() {
        sleepForAWhile("Let me think...", 1);
    }
}
