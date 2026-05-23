package com.grocery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GroceryApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroceryApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  FreshMart - Component 4: Order Management");
        System.out.println("  Running at: http://localhost:8080");
        System.out.println("  Admin: admin@freshmart.com / admin123");
        System.out.println("  Customer: john@email.com / pass123");
        System.out.println("==============================================");
    }
}
