package cl.iplacex.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class GreetingServiceTest {
    private final GreetingService service = new GreetingService();

    @Test void greetsNamedUser() { assertEquals("Hola, Jose", service.greet("Jose")); }
    @Test void usesDefaultForBlankName() { assertEquals("Hola, mundo", service.greet("  ")); }
    @Test void trimsName() { assertEquals("Hola, Ana", service.greet("  Ana  ")); }
}
