package model;

import java.util.Date;

public class Order {
    private int id;
    private int userId;
    private int totalPrice;
    private Date createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"user_id\": " + "\"" + userId + "\"" +
                ", \"total_price\": " + "\"" + totalPrice + "\"" +
                ", \"created_date\": " + "\"" + createdDate + "\"" + "}";
    }
}
