package philosophers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import static utils.ConcurrencyUtils.*;
import static utils.SocketUtils.*;
import static philosophers.Message.*;

public abstract class Philosopher {
    protected abstract int[] forks();

    protected abstract String name();

    private int counter;

    protected int limit() {
        return 5;
    }

    // initialize
    protected void init() {
        Thread.currentThread().setName(name());
        counter = 0;
    }


    private int pretty(int num) {
        return ++num;
    }

    private int[] pretty(int[] nums) {
        for (int i = 0; i < nums.length; i++)
            nums[i]++;
        return nums;
    }

    private String sendTake(Socket socket, int desiredFork) throws Exception {
        Message mes = new Message(TAKE, name(), desiredFork);
        String response;
        ObjectInputStream in;
        ObjectOutputStream out;

        out = out(socket);
        send(out, mes.toString());

        in = in(socket);
        response = receive(in);

        out.close();
        in.close();
        return response;

    }

    protected void takeForks() throws Exception {
        int[] forks = forks();

        // first need to take fork with lowest id
        Socket waiter;
        String response;
        // try to capture first fork
        int first = min(forks);
        say("Trying to capture " + pretty(first) + " fork...");
        do {
            waiter = getSocket(Waiter.PORT);
            response = sendTake(waiter, first);
            if (TAKEN.equals(response))
                sleepForAWhile("Fork " + pretty(first) + " is occupied. So, I have to wait for it ro release...");
        }
        while (TAKEN.equals(response));

        // try to capture second fork
        int second = max(forks);
        say("Trying to capture " + pretty(second) + " fork...");
        do {
            waiter = getSocket(Waiter.PORT);
            response = sendTake(waiter, second);
            if (TAKEN.equals(response))
                sleepForAWhile("Fork " + pretty(second) + " is occupied. So, I have to wait for it ro release...");
        }
        while (TAKEN.equals(response));
    }

    private int min(int... args) {
        int min = args[0];
        for (int i = 1; i < args.length; i++) {
            if (args[i] < min) min = args[i];
        }
        return min;
    }

    private int max(int... args) {
        int max = args[0];
        for (int i = 1; i < args.length; i++) {
            if (args[i] > max) max = args[i];
        }
        return max;
    }

    protected void releaseForks() throws Exception {
        int[] forks = forks();
        String response;

        // first need to release fork with highest id
        Socket waiter;

        // releasing first fork
        int first = max(forks);
        say("Giving back " + pretty(first) + " fork...");
        do {
            waiter = getSocket(Waiter.PORT);
            response = sendGiveBack(waiter, first);
        } while (UNEXPECTED.equals(response));


        // releasing second fork
        int second = min(forks);
        say("Giving back " + pretty(second) + " fork...");
        do {
            waiter = getSocket(Waiter.PORT);
            response = sendGiveBack(waiter, second);
        } while (UNEXPECTED.equals(response));
    }

    private String sendGiveBack(Socket socket, int desiredFork) throws Exception {
        Message mes = new Message(GIVE_BACK, name(), desiredFork);
        String response;
        ObjectOutputStream out;
        ObjectInputStream in;

        out = out(socket);
        send(out, mes.toString());

        in = in(socket);
        response = receive(in);

        out.close();
        in.close();
        return response;

    }

    private void greet() {
        say("Hello! My name is " + name() + "." +
                " I will use " + Arrays.toString(pretty(forks())) + " forks.");
    }

    public static void main(String[] args) throws Exception {
        Philosopher philosopher = PhilosophersFabric.of(args[0]);
        philosopher.init();
        philosopher.greet();
        while (philosopher.counter < philosopher.limit()) {
            philosopher.takeForks();
            philosopher.think();
            philosopher.eat();
            philosopher.releaseForks();
            sleepForAWhile("I'am done here! For a while...", 3.0);
            philosopher.counter++;
        }
    }

    protected void eat() {
        sleepForAWhile("Let me eat...", 2, 1.0);
    }

    protected void think() {
        sleepForAWhile("Let me think...", 1, 3.0);
    }
}
