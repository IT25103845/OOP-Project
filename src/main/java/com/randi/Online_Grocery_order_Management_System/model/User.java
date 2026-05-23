package com.randi.Online_Grocery_order_Management_System.model;

/**
 * User entity representing a registered customer or admin.
 * Demonstrates OOP: Encapsulation (private fields, public getters/setters)
 * Inherits from BaseEntity, implements Searchable (Polymorphism)
 *
 * File format: id|name|email|password|phone|role|createdAt
 */
public class User extends BaseEntity implements Searchable {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String role; // "CUSTOMER" or "ADMIN"

    public User() {
        super();
    }

    public User(String id, String name, String email, String password,
                String phone, String role, String createdAt) {
        super(id, createdAt);
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    /** Serialize to file format */
    @Override
    public String toFileString() {
        return String.join("|", id, name, email, password, phone, role, createdAt);
    }

    /** Deserialize from file format */
    public static User fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;
        return new User(parts[0], parts[1], parts[2], parts[3],
                        parts[4], parts[5], parts[6]);
    }

    /** Polymorphism: search by name or email */
    @Override
    public boolean matchesSearch(String query) {
        if (query == null || query.isEmpty()) return true;
        String q = query.toLowerCase();
        return name.toLowerCase().contains(q) || email.toLowerCase().contains(q);
    }

    @Override
    public String getDisplayName() {
        return name + " (" + email + ")";
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    // ---- Getters & Setters (Encapsulation) ----
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "', role='" + role + "'}";
    }
}
