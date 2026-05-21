package com.grocery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the FreshMart Product Management Component.
 * Uses file-based storage (txt files) for all data persistence.
 */
@SpringBootApplication
public class GroceryApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  FreshMart Product Management Component");
        System.out.println("  Running at: http://localhost:8080/products");
        System.out.println("  Default Admin: admin@freshmart.com / admin123");
        System.out.println("==============================================");
    }
}
