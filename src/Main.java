import com.sun.net.httpserver.HttpServer;
import dao.UserDao;
import handler.ItemHandler;
import handler.OrderHandler;
import handler.RegisterHandler;
import handler.UserHandler;
import security.BasicAuth;
import util.Database;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws IOException {
        createAdmin();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/users", new UserHandler())
                .setAuthenticator(new BasicAuth("users"));
        httpServer.createContext("/items", new ItemHandler())
                .setAuthenticator(new BasicAuth("items"));
        httpServer.createContext("/orders", new OrderHandler())
                .setAuthenticator(new BasicAuth("orders"));
        httpServer.createContext("/register", new RegisterHandler());
        httpServer.start();
        System.out.println("Server started on port 8080");
    }

    private static void createAdmin() {
        try(Connection connection = Database.getConnection()) {
            UserDao userDao = new UserDao(connection);
            if(!userDao.existsByPhone("admin")) {
                userDao.createAdmin();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}