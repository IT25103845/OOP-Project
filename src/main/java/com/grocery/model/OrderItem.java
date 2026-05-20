package com.grocery.model;

/**
 * OrderItem represents one product line in a placed order.
 * Separate public class for use across the application.
 */
public class OrderItem {
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;

    public OrderItem() {}

    public OrderItem(String productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getSubtotal() { return unitPrice * quantity; }
    public String getFormattedUnitPrice() { return String.format("Rs%.2f", unitPrice); }
    public String getFormattedSubtotal() { return String.format("Rs%.2f", getSubtotal()); }

    public String toFileString() {
        return productId + "~" + productName.replace("~", "-") + "~" + unitPrice + "~" + quantity;
    }

    public static OrderItem fromFileString(String s) {
        String[] p = s.split("~");
        if (p.length < 4) return null;
        return new OrderItem(p[0], p[1], Double.parseDouble(p[2]), Integer.parseInt(p[3]));
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
