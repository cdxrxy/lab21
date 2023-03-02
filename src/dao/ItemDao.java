package dao;

import exception.EmptyItemSetException;
import exception.ItemNotExistsException;
import model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ItemDao {
    private final Connection connection;

    public ItemDao(Connection connection) {
        this.connection = connection;
    }

    public void createItem(int price, String name, String manufacturer, String description, String type) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO items " +
                "(price, name, manufacturer, description, type, created_date, updated_date, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        preparedStatement.setInt(1, price);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, manufacturer);
        preparedStatement.setString(4, description);
        preparedStatement.setString(5, type);
        preparedStatement.setDate(6, new Date(Calendar.getInstance().getTimeInMillis()));
        preparedStatement.setDate(7, new Date(Calendar.getInstance().getTimeInMillis()));
        preparedStatement.setBoolean(8, true);

        preparedStatement.executeUpdate();
    }

    public List<Item> getAllItems() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM items");

        List<Item> items = new ArrayList<>();

        if(!resultSet.next()) {
            throw new EmptyItemSetException("There is no items");
        }
        items.add(Item.resultSetToItem(resultSet));

        while(resultSet.next()) {
            items.add(Item.resultSetToItem(resultSet));
        }

        return items;
    }

    public Item getItemById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM items WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            throw new ItemNotExistsException("Item doesn't exist");
        }

        return Item.resultSetToItem(resultSet);
    }
}
