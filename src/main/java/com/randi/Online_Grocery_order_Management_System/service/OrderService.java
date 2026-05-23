package com.randi.Online_Grocery_order_Management_System.service;

import com.randi.Online_Grocery_order_Management_System.model.Order;
import com.randi.Online_Grocery_order_Management_System.model.OrderItem;
import com.randi.Online_Grocery_order_Management_System.model.Cart;
import com.randi.Online_Grocery_order_Management_System.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OrderService handles order placement, tracking, and management.
 * CRUD: Create (place order), Read (history/detail), Update (status), Delete (cancel)
 */
@Service
public class OrderService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // ===== CREATE =====
    public Order placeOrder(String userId, String userName, String deliveryAddress) {
        Cart cart = cartService.getCart(userId);
        if (cart.isEmpty()) return null;

        String id = fileUtil.generateId(fileUtil.getOrdersFilePath());
        String createdAt = LocalDateTime.now().format(FORMATTER);
        Order order = new Order(id, userId, userName,
                Order.Status.CONFIRMED.name(), cart.getTotal(), deliveryAddress, createdAt);

        // Copy cart items to order & decrease stock
        cart.getItems().forEach(item -> {
            order.getItems().add(new OrderItem(
                    item.getProductId(), item.getProductName(),
                    item.getUnitPrice(), item.getQuantity()));
            productService.decreaseStock(item.getProductId(), item.getQuantity());
        });

        fileUtil.appendLine(fileUtil.getOrdersFilePath(), order.toFileString());
        cartService.clearCart(userId);
        return order;
    }

    // ===== READ =====
    public List<Order> getAllOrders() {
        List<String> lines = fileUtil.readLines(fileUtil.getOrdersFilePath());
        List<Order> orders = new ArrayList<>();
        for (String line : lines) {
            Order o = Order.fromFileString(line);
            if (o != null) orders.add(o);
        }
        return orders;
    }

    public List<Order> getOrdersByUser(String userId) {
        return getAllOrders().stream()
                .filter(o -> o.getUserId().equals(userId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Optional<Order> findById(String orderId) {
        return getAllOrders().stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst();
    }

    // ===== UPDATE =====
    public boolean updateStatus(String orderId, String newStatus) {
        Optional<Order> opt = findById(orderId);
        if (opt.isEmpty()) return false;
        Order order = opt.get();
        order.setStatus(newStatus);
        return fileUtil.updateLineById(fileUtil.getOrdersFilePath(), orderId, order.toFileString());
    }

    // ===== DELETE / CANCEL =====
    public boolean cancelOrder(String orderId, String userId) {
        Optional<Order> opt = findById(orderId);
        if (opt.isEmpty()) return false;
        Order order = opt.get();
        if (!order.getUserId().equals(userId)) return false;
        if (order.getStatus().equals("DELIVERED")) return false;
        return updateStatus(orderId, Order.Status.CANCELLED.name());
    }

    public long getTotalOrders() { return getAllOrders().size(); }

    public double getTotalRevenue() {
        return getAllOrders().stream()
                .filter(o -> !o.getStatus().equals("CANCELLED"))
                .mapToDouble(Order::getTotal).sum();
    }

    public long getPendingOrdersCount() {
        return getAllOrders().stream()
                .filter(o -> o.getStatus().equals("PENDING")).count();
    }
}
