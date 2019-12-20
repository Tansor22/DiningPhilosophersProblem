package philosophers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.SocketUtils.*;
import static utils.ConcurrencyUtils.*;
import static philosophers.Message.*;

public class Waiter {
    // Philosopher's name -> amount of food eaten
    protected static Map<String, Integer> EATING_RESULTS = new HashMap<>();
    // is i-fork taken
    protected static boolean[] FORKS_TAKEN = new boolean[5];

    protected static List<Message> REQUESTS = new ArrayList<>();

    public static final int PORT = 9876;


    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("Waiter");
        ServerSocket ss = new ServerSocket(PORT);
        while (true) {
            say("I'm ready to serve!");
            Socket philosopher = ss.accept();
            ObjectInputStream in = in(philosopher);
            // Philosopher's name, fork number
            String request = receive(in);
            Message mes = logRequest(request);
            String response = resolve(mes);
            // sending reply
            ObjectOutputStream out = out(philosopher);
            send(out, response);
        }
    }
    private static int pretty(int num) {
        return ++num;
    }
    // synchronized
    private static synchronized String resolve(Message mes) {
        String response = UNEXPECTED;
        if (TAKE.equals(mes.getSignal())) {
            if (FORKS_TAKEN[mes.getDesiredFork()]) {
                // captured
                response = TAKEN;

            } else {
                // vacant
                response = VACANT;
                FORKS_TAKEN[mes.getDesiredFork()] = true;
            }
        } else if (GIVE_BACK.equals(mes.getSignal())) {
            FORKS_TAKEN[mes.getDesiredFork()] = false;
            response = OK;
            // logging eating result
            EATING_RESULTS.merge(mes.getPhilosopherName(), 1, Integer::sum);
        }
        return response;
    }

    private static Message logRequest(String request) {
        Message mes = new Message(request);
        String action = TAKE.equals(mes.getSignal()) ? "take" : "give back";
        say(mes.getPhilosopherName() + "\'s gonna " + action + " " + pretty(mes.getDesiredFork()) + " fork.");
        REQUESTS.add(mes);
        return mes;
    }
}
