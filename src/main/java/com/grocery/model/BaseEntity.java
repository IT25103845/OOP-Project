package com.grocery.model;

/**
 * Abstract base class for all entities.
 * Demonstrates OOP concept: Abstraction & Inheritance
 */
public abstract class BaseEntity {

    protected String id;
    protected String createdAt;

    public BaseEntity() {}

    public BaseEntity(String id, String createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    // Abstract method - forces subclasses to implement serialization
    public abstract String toFileString();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "BaseEntity{id='" + id + "', createdAt='" + createdAt + "'}";
    }
}
