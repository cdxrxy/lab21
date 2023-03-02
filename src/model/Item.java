package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Item {
    private int id;
    private int price;
    private String name;
    private String manufacturer;
    private String description;
    private String type;
    private Date createdDate;
    private Date updatedDate;
    private boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static Item resultSetToItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getInt("id"));
        item.setPrice(resultSet.getInt("price"));
        item.setName(resultSet.getString("name"));
        item.setManufacturer(resultSet.getString("manufacturer"));
        item.setDescription(resultSet.getString("description"));
        item.setType(resultSet.getString("type"));
        item.setCreatedDate(resultSet.getDate("created_date"));
        item.setUpdatedDate(resultSet.getDate("updated_date"));
        item.setActive(resultSet.getBoolean("is_active"));

        return item;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"price\": " + "\"" + price + "\"" +
                ", \"name\": " + "\"" + name + "\"" +
                ", \"manufacturer\": " + "\"" + manufacturer + "\"" +
                ", \"description\": " + "\"" + description + "\"" +
                ", \"type\": " + "\"" + type + "\"" +
                ", \"created_date\": " + "\"" + createdDate + "\"" +
                ", \"updated_date\": " + "\"" + updatedDate + "\"" +
                ", \"is_active\": " + "\"" + isActive + "\"" + "}";
    }
}
