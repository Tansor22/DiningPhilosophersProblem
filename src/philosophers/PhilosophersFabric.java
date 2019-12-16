package philosophers;

public class PhilosophersFabric {
    public static Philosopher of(String name) {
        Philosopher philosopher = null;
        switch (name) {
            case "Aristotle": {
                philosopher = new Aristotle();
                break;
            }
            case "Socrates": {
                philosopher = new Socrates();
                break;
            }
            case "Plato": {
                philosopher = new Plato();
                break;
            }
            case "Pythagorean": {
                philosopher = new Pythagorean();
                break;
            }
            case "Heraclitus": {
                philosopher = new Heraclitus();
                break;
            }
        }
        return philosopher;
    }
}
