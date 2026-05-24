package com.grocery.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Order entity representing a placed customer order.
 * Demonstrates OOP: Encapsulation, Inheritance from BaseEntity
 *
 * File format: orderId|userId|userName|status|total|address|createdAt|item1~name~price~qty,...
 */
public class Order extends BaseEntity {

    public enum Status {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    private String userId;
    private String userName;
    private String status;
    private double total;
    private String deliveryAddress;
    private List<OrderItem> items;

    public Order() {
        super();
        this.items = new ArrayList<>();
        this.status = Status.PENDING.name();
    }

    public Order(String id, String userId, String userName, String status,
                 double total, String deliveryAddress, String createdAt) {
        super(id, createdAt);
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.total = total;
        this.deliveryAddress = deliveryAddress;
        this.items = new ArrayList<>();
    }

    @Override
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join("|", id, userId, userName, status,
                String.valueOf(total), deliveryAddress.replace("|", ","), createdAt));
        sb.append("|");
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).toFileString());
            if (i < items.size() - 1) sb.append(",");
        }
        return sb.toString();
    }

    public static Order fromFileString(String line) {
        String[] parts = line.split("\\|", 8);
        if (parts.length < 7) return null;
        Order order = new Order(
                parts[0], parts[1], parts[2], parts[3],
                Double.parseDouble(parts[4]), parts[5], parts[6]
        );
        if (parts.length > 7 && !parts[7].isEmpty()) {
            for (String itemStr : parts[7].split(",")) {
                OrderItem item = OrderItem.fromFileString(itemStr);
                if (item != null) order.getItems().add(item);
            }
        }
        return order;
    }

    public String getFormattedTotal() { return String.format("Rs%.2f", total); }

    public String getStatusBadgeClass() {
        return switch (status) {
            case "CONFIRMED"  -> "badge-confirmed";
            case "PROCESSING" -> "badge-processing";
            case "SHIPPED"    -> "badge-shipped";
            case "DELIVERED"  -> "badge-delivered";
            case "CANCELLED"  -> "badge-cancelled";
            
        };
    }

    // Getters & Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    @Override
    public String toString() {
        return "Order{id='" + id + "', userId='" + userId + "', status='" + status + "', total=" + total + "}";
    }
}
