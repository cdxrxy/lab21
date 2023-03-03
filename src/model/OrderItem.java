package model;

public class OrderItem {
    private int id;
    private int orderId;
    private int itemId;
    private int quantity;
    private int subtotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"order_id\": " + "\"" + orderId + "\"" +
                ", \"item_id\": " + "\"" + itemId + "\"" +
                ", \"quantity\": " + "\"" + quantity + "\"" +
                ", \"subtotal\": " + "\"" + subtotal + "\"" + "}";
    }
}
