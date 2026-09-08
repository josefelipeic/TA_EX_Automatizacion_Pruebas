package cl.iplacex.app;

public final class GreetingService {
    public String greet(String name) {
        if (name == null || name.isBlank()) return "Hola, mundo";
        return "Hola, " + name.trim();
    }
}
