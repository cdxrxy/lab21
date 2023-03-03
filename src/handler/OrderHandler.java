package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ItemDao;
import dao.OrderDao;
import dao.UserDao;
import model.OrderItem;
import model.User;
import util.Database;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class OrderHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 400);
            return;
        }

        try(Connection connection = Database.getConnection()) {
            OrderDao orderDao = new OrderDao(connection, new ItemDao(connection));
            UserDao userDao = new UserDao(connection);

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                handlePostRequest(exchange, orderDao, userDao);
                return;
            }

            ErrorHandler.handleError(exchange, "Method Not Allowed", 405);
        }
        catch (SQLException | NumberFormatException e) {
            ErrorHandler.handleError(exchange, "Invalid Request body", 400);
            e.printStackTrace();
        }
        catch (Exception e) {
            ErrorHandler.handleError(exchange, "Something went wrong", 500);
            e.printStackTrace();
        }
    }

    private void handlePostRequest(HttpExchange exchange, OrderDao orderDao, UserDao userDao) throws IOException, SQLException {
        String phone = exchange.getPrincipal().getUsername();
        User user = userDao.getUserByPhone(phone);

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
        body = body.substring(body.indexOf("[") + 1, body.length() - 1);
        String finalBody = body.replace("{", "").replace("}", "");

        Map<String, String> itemMap = new HashMap<>();
        List<OrderItem> orderItems = new ArrayList<>();
        Arrays.stream(finalBody.split(",")).forEach(item -> {
            if(!itemMap.isEmpty()) {
                OrderItem orderItem = new OrderItem();
                if(itemMap.get("itemId") != null) {
                    orderItem.setItemId(Integer.parseInt(itemMap.get("itemId")));
                    orderItem.setQuantity(Integer.parseInt(item.split(":")[1].trim()));
                }
                else if(itemMap.get("quantity") != null) {
                    orderItem.setQuantity(Integer.parseInt(itemMap.get("quantity")));
                    orderItem.setItemId(Integer.parseInt(item.split(":")[1].trim()));
                }
                orderItems.add(orderItem);
                itemMap.clear();
            }
            else {
                itemMap.put(item.split(":")[0].trim(), item.split(":")[1].trim());
            }
        });

        orderDao.createOrder(user.getId(), orderItems);

        String response = "{\"message\": \"Order successfully created\", \"status\": 200}";

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }
}
