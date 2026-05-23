package com.grocery.model;

/**
 * Searchable interface for entities that can be searched.
 * Demonstrates OOP concept: Polymorphism via Interface
 */
public interface Searchable {
    /**
     * Returns true if the entity matches the search query.
     * Each class implements this differently — demonstrating polymorphism.
     */
    boolean matchesSearch(String query);

    /**
     * Returns a display-friendly string for search results.
     */
    String getDisplayName();
}
