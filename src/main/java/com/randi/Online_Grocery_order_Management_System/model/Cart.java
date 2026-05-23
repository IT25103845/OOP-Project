package com.randi.Online_Grocery_order_Management_System.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cart entity representing a user's shopping cart.
 * Demonstrates OOP: Encapsulation, Composition (contains CartItems)
 *
 * File format: userId|productId:name:price:qty,productId:name:price:qty,...
 */
public class Cart {

    private String userId;
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public void addItem(String productId, String productName, double unitPrice, int quantity) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(productId, productName, unitPrice, quantity));
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    public void updateQuantity(String productId, int newQty) {
        if (newQty <= 0) {
            removeItem(productId);
            return;
        }
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(newQty);
                return;
            }
        }
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public String getFormattedTotal() {
        return String.format("$%.2f", getTotal());
    }

    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    /** Serialize to file */
    public String toFileString() {
        if (items.isEmpty()) return userId + "|";
        StringBuilder sb = new StringBuilder(userId).append("|");
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).toFileString());
            if (i < items.size() - 1) sb.append(",");
        }
        return sb.toString();
    }

    /** Deserialize from file */
    public static Cart fromFileString(String line) {
        if (line.startsWith("#")) return null;
        String[] parts = line.split("\\|", 2);
        Cart cart = new Cart(parts[0]);
        if (parts.length > 1 && !parts[1].isEmpty()) {
            for (String itemStr : parts[1].split(",")) {
                CartItem item = CartItem.fromFileString(itemStr);
                if (item != null) cart.getItems().add(item);
            }
        }
        return cart;
    }

    // Getters & Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
