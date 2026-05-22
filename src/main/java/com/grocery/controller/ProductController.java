package com.grocery.controller;

import com.grocery.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Support controller for Component 3: Shopping Cart.
 * Product browsing is included only so users can choose items to add to the cart.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String category) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("products", productService.searchProducts(search));
            model.addAttribute("search", search);
        } else if (category != null && !category.isBlank()) {
            model.addAttribute("products", productService.getByCategory(category));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }

        model.addAttribute("categories", productService.getAllCategories());
        return "products/list";
    }
}
