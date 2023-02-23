package handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ErrorHandler {
    public static void handleError(HttpExchange exchange, String message, int status) throws IOException {
        String response = "{\"message\": " + "\"" + message + "\"" + ", \"status\": " + status + "}";

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }
}
