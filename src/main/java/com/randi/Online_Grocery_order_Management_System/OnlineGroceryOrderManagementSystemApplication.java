package com.randi.Online_Grocery_order_Management_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;


import org.springframework.context.annotation.ComponentScan;
@ComponentScan(basePackages = {"com.randi.Online_Grocery_order_Management_System", "admin"})

@SpringBootApplication
public class OnlineGroceryOrderManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineGroceryOrderManagementSystemApplication.class, args);
	}

}
