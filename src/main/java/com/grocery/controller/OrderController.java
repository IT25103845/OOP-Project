package com.grocery.controller;

import com.grocery.model.Order;
import com.grocery.model.User;
import com.grocery.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String myOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getOrdersByUser(user.getId()));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable String id, HttpSession session, Model model, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = orderService.findById(id).orElse(null);
        if (order == null) {
            ra.addFlashAttribute("error", "Order not found.");
            return "redirect:/orders";
        }
        if (!user.isAdmin() && !order.getUserId().equals(user.getId())) {
            ra.addFlashAttribute("error", "You cannot view that order.");
            return "redirect:/orders";
        }
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        return "orders/detail";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        boolean cancelled = orderService.cancelOrder(id, user.getId());
        ra.addFlashAttribute(cancelled ? "success" : "error",
                cancelled ? "Order cancelled." : "Cannot cancel this order.");
        return "redirect:/orders/" + id;
    }

    @PostMapping("/create-sample")
    public String createSampleOrder(@RequestParam String deliveryAddress,
                                    @RequestParam String productName,
                                    @RequestParam double unitPrice,
                                    @RequestParam int quantity,
                                    HttpSession session,
                                    RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Order order = orderService.createManualOrder(user.getId(), user.getName(), deliveryAddress,
                productName, unitPrice, quantity);
        ra.addFlashAttribute("success", "Sample order #" + order.getId() + " created.");
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/admin")
    public String adminOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!user.isAdmin()) return "redirect:/orders";
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("totalRevenue", String.format("Rs%.2f", orderService.getTotalRevenue()));
        model.addAttribute("pendingCount", orderService.getPendingOrdersCount());
        model.addAttribute("totalOrders", orderService.getTotalOrders());
        return "orders/admin";
    }

    @PostMapping("/admin/status/{id}")
    public String updateStatus(@PathVariable String id,
                               @RequestParam String status,
                               HttpSession session,
                               RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!user.isAdmin()) return "redirect:/orders";
        boolean updated = orderService.updateStatus(id, status);
        ra.addFlashAttribute(updated ? "success" : "error",
                updated ? "Order status updated." : "Order not found.");
        return "redirect:/orders/admin";
    }
}
