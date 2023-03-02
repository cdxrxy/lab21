package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ItemDao;
import dao.UserDao;
import exception.EmptyItemSetException;
import exception.ItemNotExistsException;
import util.Database;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ItemHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 400);
            return;
        }

        try(Connection connection = Database.getConnection()) {
            ItemDao itemDao = new ItemDao(connection);

            if("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                handlePostRequest(exchange, itemDao);
                return;
            }
            if("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleGetRequest(exchange, itemDao);
                return;
            }

            ErrorHandler.handleError(exchange, "Method Not Allowed", 405);
        } catch (EmptyItemSetException | ItemNotExistsException e) {
            ErrorHandler.handleError(exchange, e.getMessage(), 400);
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            ErrorHandler.handleError(exchange, "Invalid Request Body", 400);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorHandler.handleError(exchange, "Something went wrong", 500);
            e.printStackTrace();
        }
    }

    private void handlePostRequest(HttpExchange exchange, ItemDao itemDao) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath().substring(1);
        if(path.split("/").length > 1) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }

        InputStream is = exchange.getRequestBody();
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

        itemDao.createItem(Integer.parseInt(bodyMap.get("price")), bodyMap.get("name"), bodyMap.get("manufacturer")
                , bodyMap.get("description"), bodyMap.get("type"));

        String response = "{\"message\": \"Item successfully created\", \"status\": 200}";

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }

    private void handleGetRequest(HttpExchange httpExchange, ItemDao itemDao) throws IOException, SQLException {
        String response;

        String path = httpExchange.getRequestURI().getPath().substring(1);
        if(path.split("/").length > 1) {
            int id = Integer.parseInt(path.split("/")[1]);
            if (id > 0) {
                response = itemDao.getItemById(id).toString();
            }
            else {
                ErrorHandler.handleError(httpExchange, "Invalid Id", 400);
                return;
            }
        }
        else {
            response = itemDao.getAllItems().toString();
        }

        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }
}
