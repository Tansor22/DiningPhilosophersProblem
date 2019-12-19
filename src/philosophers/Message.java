package philosophers;

// signal - philosopher - fork
public class Message {
    // space
    private static final String DELIMITER = " ";

    private String philosopherName;
    private int desiredFork;
    private String signal;

    public static final String TAKEN = "#taken";
    public static final String VACANT = "#vacant";
    public static final String UNEXPECTED = "#unexpected";
    public static final String OK = "#ok";
    public static final String TAKE = "take";
    public static final String GIVE_BACK = "give_back";



    public Message(String message) {
        String[] data = message.split(DELIMITER);
        signal = data[0];
        philosopherName = data[1];
        // exception may occur
        desiredFork = Integer.parseInt(data[2]);

    }

    public Message(String signal, String philosopherName, int desiredFork) {
        this.philosopherName = philosopherName;
        this.desiredFork = desiredFork;
        this.signal = signal;
    }

    public String getPhilosopherName() {
        return philosopherName;
    }

    public int getDesiredFork() {
        return desiredFork;
    }
    public String getSignal() {
        return signal;
    }

    @Override
    public String toString() {
        return signal + ' ' + philosopherName + ' ' + desiredFork;

    }
}
