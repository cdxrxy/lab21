package dao;

import exception.ItemNotExistsException;
import model.Item;
import model.OrderItem;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

public class OrderDao {
    private final Connection connection;
    private final ItemDao itemDao;

    public OrderDao(Connection connection, ItemDao itemDao) {
        this.connection = connection;
        this.itemDao = itemDao;
    }

    public void createOrder(int userId, List<OrderItem> orderItems) throws SQLException {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            if (!itemDao.existsById(orderItem.getItemId())) {
                throw new ItemNotExistsException("Item doesn't exist");
            }
            Item item = itemDao.getItemById(orderItem.getItemId());
            int subtotal = orderItem.getQuantity() * item.getPrice();
            totalPrice += subtotal;
            orderItem.setSubtotal(subtotal);
        }

        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO orders (user_id, total_price, created_date)" +
                        "VALUES (?, ?, ?)");
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, totalPrice);
        preparedStatement.setDate(3, new Date(Calendar.getInstance().getTimeInMillis()));
        preparedStatement.executeUpdate();

        PreparedStatement getLastOrder = connection.prepareStatement("SELECT id FROM orders",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet lastOrder = getLastOrder.executeQuery();
        lastOrder.last();
        int orderId = lastOrder.getInt("id");

        PreparedStatement createOrderItem = connection
                .prepareStatement("INSERT INTO orders_items (order_id, item_id, quantity, subtotal) VALUES (?, ?, ?, ?)");
        createOrderItem.setInt(1, orderId);

        for (OrderItem orderItem : orderItems) {
            createOrderItem.setInt(2, orderItem.getItemId());
            createOrderItem.setInt(3, orderItem.getQuantity());
            createOrderItem.setInt(4, orderItem.getSubtotal());
            createOrderItem.executeUpdate();
        }
    }
}
