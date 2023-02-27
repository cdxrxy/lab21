package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.UserDao;
import util.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterHandler implements HttpHandler {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public RegisterHandler(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 1) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }

        try(Connection connection = Database.getDB(dbUrl, dbUser, dbPassword).getConnection()) {
            UserDao userDao = new UserDao(connection);

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                UserHandler.handlePostRequest(exchange, userDao);
                return;
            }

            ErrorHandler.handleError(exchange, "Method Not Allowed", 405);
        }
        catch (SQLException e) {
            ErrorHandler.handleError(exchange, "Invalid request body", 400);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorHandler.handleError(exchange, "Something went wrong", 500);
            e.printStackTrace();
        }
    }
}
