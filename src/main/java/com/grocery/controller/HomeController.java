package com.grocery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home route for Component 3.
 * Redirects users to the product list so they can add items to the cart.
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }
}
