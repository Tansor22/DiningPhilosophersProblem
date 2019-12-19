package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConcurrencyUtils {
    public static void sleep(double seconds) {
        Function<Double, Long> secondsConverter = doubleSeconds -> (long) (doubleSeconds * 1000);
        try {
            Thread.sleep(secondsConverter.apply(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(String message, double seconds) {
        say(message);
        sleep(seconds);
    }

    public static void sleepForAWhile(String message) {
        // 2 secs max
        double secs = Math.random() * 3;
        say(message);
        sleep(secs);
    }

    public static void sleepForAWhile(String message, int indentSize) {
        // 2 secs max
        double secs = Math.random() * 2;
        say(message, indentSize);
        sleep(secs);
    }

    public static void say(String message) {
        say(message, 0);
    }

    public static void say(String message, int indentSize) {
        Supplier<String> messageSupplier = () -> {
            String threadName = Thread.currentThread().getName();
            StringBuilder sb = new StringBuilder();

            // also works
            // DateFormat df = new SimpleDateFormat("HH:mm::ss");
            //System.out.println(df.format(Calendar.getInstance().getTime());

            if (indentSize > 0)
                for (int i = 0; i < indentSize; i++)
                    sb.append('\t');
            sb
                    .append(LocalTime.now())
                    .append(" -> ")
                    .append(threadName)
                    .append(" says: ")
                    .append(message);
            return sb.toString();
        };
        System.out.println(messageSupplier.get());
    }

    public static void say(String message, double seconds) {
        say(message);
        sleep(seconds);
    }
}
