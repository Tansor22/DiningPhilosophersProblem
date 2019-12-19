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
        return 10;
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

    private void sendTake(Socket socket, int desiredFork) {
        Message mes = new Message(TAKE, name(), desiredFork);
        ObjectOutputStream out = out(socket);
        // TODO should use serverSocket.accept ??? AND THE ANSWER IS ... YES
        String response;
        do {
            send(out, mes.toString());
            ObjectInputStream in = in(socket);
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = receive(in);
            if (TAKEN.equals(response))
                sleepForAWhile("Fork " + pretty(desiredFork) + " is occupied. So, I have to wait for it ro release...");
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (TAKEN.equals(response));
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void takeForks() {
        int[] forks = forks();

        // first need to take fork with lowest id
        Socket waiter = getSocket(Waiter.PORT);
        // try to capture first fork
        int first = min(forks);
        say("Trying to capture " + pretty(first) + " fork...");
        sendTake(waiter, first);
        try {
            waiter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        waiter = getSocket(Waiter.PORT);
        // try to capture second fork
        int second = max(forks);
        say("Trying to capture " + pretty(second) + " fork...");
        sendTake(waiter, second);
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

    protected void releaseForks() {
        int[] forks = forks();


        // first need to release fork with highest id
        Socket waiter = getSocket(Waiter.PORT);

        // releasing first fork
        int first = max(forks);
        say("Giving back " + pretty(first) + " fork...");
        sendGiveBack(waiter, first);

        try {
            waiter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        waiter = getSocket(Waiter.PORT);
        // releasing second fork
        int second = min(forks);
        say("Giving back " + pretty(second) + " fork...");
        sendGiveBack(waiter, second);
    }

    private void sendGiveBack(Socket socket, int desiredFork) {
        Message mes = new Message(GIVE_BACK, name(), desiredFork);
        ObjectOutputStream out = out(socket);
        String response;
        do {
            send(out, mes.toString());
            ObjectInputStream in = in(socket);
            response = receive(in);
            sleepForAWhile("Fork " + pretty(desiredFork) + " has been released!");
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (UNEXPECTED.equals(response));
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void greet() {
        say("Hello! My name is " + name() + "." +
                " I will use " + Arrays.toString(pretty(forks())) + " forks.");
    }

    public static void main(String[] args) {
        Philosopher philosopher = PhilosophersFabric.of(args[0]);
        philosopher.init();
        philosopher.greet();
        while (philosopher.counter < philosopher.limit()) {
            philosopher.takeForks();
            philosopher.think();
            philosopher.eat();
            philosopher.releaseForks();
            say("I'am done here! For a while...");
        }
    }

    protected void eat() {
        sleepForAWhile("Let me eat...", 2);
    }

    protected void think() {
        sleepForAWhile("Let me think...", 1);
    }
}
