package com.grocery.service;

import com.grocery.model.Order;
import com.grocery.model.OrderItem;
import com.grocery.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Component 4: Order Management.
 * Handles order history, order details, cancellation, admin status updates,
 * and file-based order persistence.
 */
@Service
public class OrderService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private FileUtil fileUtil;

    public List<Order> getAllOrders() {
        List<String> lines = fileUtil.readLines(fileUtil.getOrdersFilePath());
        List<Order> orders = new ArrayList<>();
        for (String line : lines) {
            Order order = Order.fromFileString(line);
            if (order != null) orders.add(order);
        }
        orders.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return orders;
    }

    public List<Order> getOrdersByUser(String userId) {
        return getAllOrders().stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Order> findById(String orderId) {
        return getAllOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
    }

    public Order createManualOrder(String userId, String userName, String deliveryAddress,
                                   String productName, double unitPrice, int quantity) {
        String id = fileUtil.generateId(fileUtil.getOrdersFilePath());
        String createdAt = LocalDateTime.now().format(FORMATTER);
        Order order = new Order(id, userId, userName, Order.Status.CONFIRMED.name(),
                unitPrice * quantity, deliveryAddress, createdAt);
        order.getItems().add(new OrderItem("MANUAL", productName, unitPrice, quantity));
        fileUtil.appendLine(fileUtil.getOrdersFilePath(), order.toFileString());
        return order;
    }

    public boolean updateStatus(String orderId, String newStatus) {
        Optional<Order> existing = findById(orderId);
        if (existing.isEmpty()) return false;
        Order order = existing.get();
        order.setStatus(newStatus);
        return fileUtil.updateLineById(fileUtil.getOrdersFilePath(), orderId, order.toFileString());
    }

    public boolean cancelOrder(String orderId, String userId) {
        Optional<Order> existing = findById(orderId);
        if (existing.isEmpty()) return false;
        Order order = existing.get();
        if (!order.getUserId().equals(userId)) return false;
        if ("DELIVERED".equals(order.getStatus())) return false;
        if ("CANCELLED".equals(order.getStatus())) return false;
        return updateStatus(orderId, Order.Status.CANCELLED.name());
    }

    public long getTotalOrders() {
        return getAllOrders().size();
    }

    public double getTotalRevenue() {
        return getAllOrders().stream()
                .filter(order -> !"CANCELLED".equals(order.getStatus()))
                .mapToDouble(Order::getTotal)
                .sum();
    }

    public long getPendingOrdersCount() {
        return getAllOrders().stream()
                .filter(order -> "PENDING".equals(order.getStatus()) || "CONFIRMED".equals(order.getStatus()))
                .count();
    }
}
