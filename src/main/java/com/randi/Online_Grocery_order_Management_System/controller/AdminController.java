package com.randi.Online_Grocery_order_Management_System.controller;

import com.randi.Online_Grocery_order_Management_System.model.Order;
import com.randi.Online_Grocery_order_Management_System.model.Product;
import com.randi.Online_Grocery_order_Management_System.model.User;
import com.randi.Online_Grocery_order_Management_System.service.OrderService;
import com.randi.Online_Grocery_order_Management_System.service.ProductService;
import com.randi.Online_Grocery_order_Management_System.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component 6: Admin Dashboard.
 * Handles dashboard statistics and admin-only management pages for users,
 * products, and orders.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private ProductService productService;
    @Autowired private OrderService orderService;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.isAdmin();
    }

    private void addCurrentUser(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        List<User> users = userService.getAllUsers();
        List<Product> products = productService.getAllProducts();
        List<Order> orders = orderService.getAllOrders();

        List<Product> lowStockProducts = products.stream()
                .filter(p -> p.getStock() <= 10)
                .sorted(Comparator.comparingInt(Product::getStock))
                .limit(5)
                .collect(Collectors.toList());

        List<Order> recentOrders = orders.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());

        addCurrentUser(session, model);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalCustomers", userService.getTotalCustomers());
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("pendingOrders", orderService.getPendingOrdersCount());
        model.addAttribute("totalRevenue", String.format("Rs%.2f", orderService.getTotalRevenue()));
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("recentOrders", recentOrders);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(HttpSession session, Model model,
                        @RequestParam(required = false) String search) {
        if (!isAdmin(session)) return "redirect:/login";
        addCurrentUser(session, model);
        model.addAttribute("users", search != null && !search.isBlank()
                ? userService.searchUsers(search)
                : userService.getAllUsers());
        model.addAttribute("search", search);
        return "admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable String id,
                             HttpSession session,
                             RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        User current = (User) session.getAttribute("user");
        if (current != null && current.getId().equals(id)) {
            ra.addFlashAttribute("error", "You cannot delete the currently logged-in admin account.");
        } else {
            userService.deleteUser(id);
            ra.addFlashAttribute("success", "User deleted successfully.");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/products")
    public String products(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        addCurrentUser(session, model);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/products";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String name,
                             @RequestParam String category,
                             @RequestParam double price,
                             @RequestParam int stock,
                             @RequestParam String description,
                             @RequestParam String imageEmoji,
                             HttpSession session,
                             RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        productService.addProduct(name, category, price, stock, description, imageEmoji);
        ra.addFlashAttribute("success", "Product added successfully.");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable String id,
                                @RequestParam String name,
                                @RequestParam String category,
                                @RequestParam double price,
                                @RequestParam int stock,
                                @RequestParam String description,
                                @RequestParam String imageEmoji,
                                HttpSession session,
                                RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        productService.updateProduct(id, name, category, price, stock, description, imageEmoji);
        ra.addFlashAttribute("success", "Product updated successfully.");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable String id,
                                HttpSession session,
                                RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        productService.deleteProduct(id);
        ra.addFlashAttribute("success", "Product deleted successfully.");
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        addCurrentUser(session, model);
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("totalRevenue", String.format("Rs%.2f", orderService.getTotalRevenue()));
        model.addAttribute("pendingCount", orderService.getPendingOrdersCount());
        return "admin/orders";
    }

    @PostMapping("/orders/status/{id}")
    public String updateOrderStatus(@PathVariable String id,
                                    @RequestParam String status,
                                    HttpSession session,
                                    RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        orderService.updateStatus(id, status);
        ra.addFlashAttribute("success", "Order status updated successfully.");
        return "redirect:/admin/orders";
    }
}
