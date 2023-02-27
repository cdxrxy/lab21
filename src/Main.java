import com.sun.net.httpserver.HttpServer;
import dao.UserDao;
import handler.RegisterHandler;
import handler.UserHandler;
import security.BasicAuth;
import util.Database;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class Main {
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/code21";
    private static final String dbUser = "root";
    private static final String dbPassword = "Qwerty1";

    public static void main(String[] args) throws IOException {
        createAdmin();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/users", new UserHandler(dbUrl, dbUser, dbPassword))
                .setAuthenticator(new BasicAuth("users"));
        httpServer.createContext("/register", new RegisterHandler(dbUrl, dbUser, dbPassword));
        httpServer.start();
        System.out.println("Server started on port 8080");
    }

    private static void createAdmin() {
        try(Connection connection = Database.getDB(dbUrl, dbUser, dbPassword).getConnection()) {
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