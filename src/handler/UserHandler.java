package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.UserDao;
import exception.EmptyUserSetException;
import exception.UserAlreadyExistsException;
import exception.UserNotExistsException;
import util.Database;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserHandler implements HttpHandler {
    private final String url;
    private final String user;
    private final String password;

    public UserHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }

        try (Connection connection = Database.getDB(url, user, password).getConnection()) {
            UserDao userDao = new UserDao(connection);

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleGetRequest(exchange, userDao);
                return;
            }
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                handlePostRequest(exchange, userDao);
                return;
            }

            ErrorHandler.handleError(exchange, "Method Not Allowed", 405);
        } catch (EmptyUserSetException | UserNotExistsException | UserAlreadyExistsException e) {
            ErrorHandler.handleError(exchange, e.getMessage(), 400);
            e.printStackTrace();
        } catch (SQLException e) {
            ErrorHandler.handleError(exchange, "Invalid request body", 400);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            ErrorHandler.handleError(exchange, "Invalid id", 400);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorHandler.handleError(exchange, "Something went wrong", 500);
            e.printStackTrace();
        }
    }

    private void handlePostRequest(HttpExchange httpExchange, UserDao userDao)
            throws IOException, SQLException, NoSuchAlgorithmException {
        String path = httpExchange.getRequestURI().getPath().substring(1);
        if(path.split("/").length > 1) {
            ErrorHandler.handleError(httpExchange, "Not Found", 404);
            return;
        }

        InputStream is = httpExchange.getRequestBody();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();

        String str;
        while((str = br.readLine()) != null) {
            sb.append(str);
        }

        String body = sb.toString();
        body = body.substring(1, body.length() - 1);
        body = body.replaceAll("\"", "");

        Map<String, String> bodyMap = new HashMap<>();
        Arrays.stream(body.split(","))
                .forEach(string -> bodyMap.put(string.split(":")[0].trim(), string.split(":")[1].trim()));

        userDao.createUser(bodyMap.get("firstname"), bodyMap.get("lastname"),
                bodyMap.get("phone"), bodyMap.get("password"), bodyMap.get("address"));

        String response = "{\"message\": \"User successfully created\", \"status\": 200}";

        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }

    private void handleGetRequest(HttpExchange httpExchange, UserDao userDao) throws IOException, SQLException {
        String response;

        String path = httpExchange.getRequestURI().getPath().substring(1);
        if(path.split("/").length > 1) {
            long id = Long.parseLong(path.split("/")[1]);
            if (id > 0) {
                response = userDao.getUserById(id).toString();
            }
            else {
                ErrorHandler.handleError(httpExchange, "Invalid Id", 400);
                return;
            }
        }
        else {
            response = userDao.getAllUsers().toString();
        }

        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }
}