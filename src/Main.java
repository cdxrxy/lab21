import com.sun.net.httpserver.HttpServer;
import handler.RegisterHandler;
import handler.UserHandler;
import security.BasicAuth;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/code21";
    private static final String dbUser = "root";
    private static final String dbPassword = "Qwerty1";

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/users", new UserHandler(dbUrl, dbUser, dbPassword))
                .setAuthenticator(new BasicAuth("users"));
        httpServer.createContext("/register", new RegisterHandler(dbUrl, dbUser, dbPassword));
        httpServer.start();
        System.out.println("Server started on port 8080");
    }
}