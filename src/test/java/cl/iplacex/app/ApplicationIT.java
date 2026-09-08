package cl.iplacex.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.sun.net.httpserver.HttpServer;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationIT {
    private HttpServer server;
    private int port;

    @BeforeEach void setUp() throws Exception {
        try (ServerSocket socket = new ServerSocket(0)) { port = socket.getLocalPort(); }
        server = Application.start(port);
    }
    @AfterEach void tearDown() { if (server != null) server.stop(0); }

    @Test void exposesHealthEndpoint() throws Exception { assertEquals("UP", get("/health")); }
    @Test void exposesGreetingEndpoint() throws Exception { assertEquals("Hola, Jose Felipe", get("/api/greeting?name=Jose%20Felipe")); }

    private String get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
