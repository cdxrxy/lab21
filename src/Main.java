import com.sun.net.httpserver.HttpServer;
import handler.UserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    static final String url = "jdbc:postgresql://localhost:5432/code21";
    static final String user = "root";
    static final String password = "Qwerty1";

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/users", new UserHandler(url, user, password));
        System.out.println("Server started on port 8080");
        httpServer.start();
    }
}