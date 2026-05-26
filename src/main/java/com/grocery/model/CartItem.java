package com.grocery.model;

/**
 * CartItem represents one product line in a shopping cart.
 * Demonstrates OOP: Encapsulation
 */
public class CartItem {

    

    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;

    public CartItem() {}

    public CartItem(String productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }

    public String toFileString() {
        return productId + ":" + productName.replace(":", "-") + ":" + unitPrice + ":" + quantity;
    }

    public static CartItem fromFileString(String s) {
        String[] p = s.split(":");
        if (p.length < 4) return null;
        return new CartItem(p[0], p[1], Double.parseDouble(p[2]), Integer.parseInt(p[3]));
    }

    // Getters & Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getFormattedUnitPrice() { return String.format("Rs%.2f", unitPrice); }
    public String getFormattedSubtotal() { return String.format("Rs%.2f", getSubtotal()); }
}
