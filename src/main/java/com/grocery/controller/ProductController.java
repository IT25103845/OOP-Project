package com.grocery.controller;

import com.grocery.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Component 2: Product Management.
 * Handles product listing, search, filtering, detail view, and admin CRUD.
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

    @GetMapping("/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        productService.findById(id).ifPresent(product -> model.addAttribute("product", product));
        return "products/detail";
    }

    @GetMapping("/admin")
    public String adminProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/admin";
    }

    @PostMapping("/admin/add")
    public String addProduct(@RequestParam String name,
                             @RequestParam String category,
                             @RequestParam double price,
                             @RequestParam int stock,
                             @RequestParam String description,
                             @RequestParam String imageEmoji,
                             RedirectAttributes redirectAttributes) {
        productService.addProduct(name, category, price, stock, description, imageEmoji);
        redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        return "redirect:/products/admin";
    }

    @PostMapping("/admin/update/{id}")
    public String updateProduct(@PathVariable String id,
                                @RequestParam String name,
                                @RequestParam String category,
                                @RequestParam double price,
                                @RequestParam int stock,
                                @RequestParam String description,
                                @RequestParam String imageEmoji,
                                RedirectAttributes redirectAttributes) {
        boolean updated = productService.updateProduct(id, name, category, price, stock, description, imageEmoji);
        redirectAttributes.addFlashAttribute(updated ? "success" : "error",
                updated ? "Product updated successfully!" : "Product not found.");
        return "redirect:/products/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes redirectAttributes) {
        boolean deleted = productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute(deleted ? "success" : "error",
                deleted ? "Product deleted successfully." : "Product not found.");
        return "redirect:/products/admin";
    }
}
