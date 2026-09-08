package cl.iplacex.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public final class Application {
    private Application() { }

    public static HttpServer start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        GreetingService service = new GreetingService();
        server.createContext("/health", exchange -> respond(exchange, 200, "UP"));
        server.createContext("/api/greeting", exchange -> {
            if (!"GET".equals(exchange.getRequestMethod())) { respond(exchange, 405, "Method Not Allowed"); return; }
            String name = query(exchange.getRequestURI().getRawQuery()).get("name");
            respond(exchange, 200, service.greet(name));
        });
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();
        return server;
    }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("APP_PORT", "8080"));
        start(port);
        System.out.println("Aplicacion iniciada en http://localhost:" + port);
    }

    private static Map<String,String> query(String raw) {
        Map<String,String> values = new HashMap<>();
        if (raw == null || raw.isBlank()) return values;
        for (String pair : raw.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length == 2 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            values.put(key, value);
        }
        return values;
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var output = exchange.getResponseBody()) { output.write(bytes); }
    }
}
