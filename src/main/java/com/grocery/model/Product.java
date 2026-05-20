package com.grocery.model;

/**
 * Product entity representing a grocery item.
 * Demonstrates OOP: Encapsulation, Inheritance, Polymorphism
 *
 * File format: id|name|category|price|stock|description|imageEmoji|createdAt
 */
public class Product extends BaseEntity implements Searchable {

    private String name;
    private String category;
    private double price;
    private int stock;
    private String description;
    private String imageEmoji;

    public Product() {
        super();
    }

    public Product(String id, String name, String category, double price,
                   int stock, String description, String imageEmoji, String createdAt) {
        super(id, createdAt);
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imageEmoji = imageEmoji;
    }

    /** Serialize to file format */
    @Override
    public String toFileString() {
        return String.join("|", id, name, category,
                String.valueOf(price), String.valueOf(stock),
                description, imageEmoji, createdAt);
    }

    /** Deserialize from file format */
    public static Product fromFileString(String line) {
        // Limit to 8 parts so a description containing '|' doesn't shift the fields
        String[] parts = line.split("\\|", 8);
        if (parts.length < 8) return null;
        return new Product(
                parts[0], parts[1], parts[2],
                Double.parseDouble(parts[3]),
                Integer.parseInt(parts[4]),
                parts[5], parts[6], parts[7]
        );
    }

    /** Polymorphism: search by name, category, or description */
    @Override
    public boolean matchesSearch(String query) {
        if (query == null || query.isEmpty()) return true;
        String q = query.toLowerCase();
        return name.toLowerCase().contains(q)
                || category.toLowerCase().contains(q)
                || description.toLowerCase().contains(q);
    }

    @Override
    public String getDisplayName() {
        return name + " - Rs " + String.format("%.2f", price);
    }

    public boolean isInStock() {
        return stock > 0;
    }

    public String getFormattedPrice() {
        return String.format("Rs%.2f", price);
    }

    // ---- Getters & Setters ----
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageEmoji() { return imageEmoji; }
    public void setImageEmoji(String imageEmoji) { this.imageEmoji = imageEmoji; }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}
