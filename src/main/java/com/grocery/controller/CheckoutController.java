package com.grocery.controller;

import com.grocery.model.Cart;
import com.grocery.model.Order;
import com.grocery.model.User;
import com.grocery.service.CartService;
import com.grocery.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckoutController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping({"/checkout", "/orders/checkout"})
    public String checkoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Cart cart = cartService.getCart(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("cart", cart);
        return "checkout/index";
    }

    @PostMapping({"/checkout/place", "/orders/place"})
    public String placeOrder(@RequestParam String deliveryAddress,
                             @RequestParam String paymentMethod,
                             @RequestParam(required = false) String cardNumber,
                             @RequestParam(required = false) String cardExpiry,
                             @RequestParam(required = false) String cardCvv,
                             @RequestParam(required = false) String cardName,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Cart cart = cartService.getCart(user.getId());
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty. Load the demo cart and try again.");
            return "redirect:/cart";
        }

        String validationError = validatePayment(paymentMethod, cardNumber, cardExpiry, cardCvv, cardName);
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("error", validationError);
            return "redirect:/checkout";
        }

        Order order = orderService.placeOrder(user.getId(), user.getName(), deliveryAddress);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Checkout failed. Please check your cart and try again.");
            return "redirect:/checkout";
        }

        String message = "card".equals(paymentMethod)
                ? "Payment successful. Your card was validated and the order is confirmed."
                : "Order confirmed with Cash on Delivery.";
        redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/checkout/confirmation/" + order.getId();
    }

    @GetMapping("/checkout/confirmation/{id}")
    public String confirmation(@PathVariable String id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        return orderService.findById(id)
                .map(order -> {
                    if (!order.getUserId().equals(user.getId()) && !user.isAdmin()) return "redirect:/checkout";
                    model.addAttribute("user", user);
                    model.addAttribute("order", order);
                    return "checkout/confirmation";
                })
                .orElse("redirect:/checkout");
    }

    private String validatePayment(String paymentMethod, String cardNumber, String cardExpiry, String cardCvv, String cardName) {
        if (paymentMethod == null || paymentMethod.isBlank()) return "Please select a payment method.";
        if ("cash".equals(paymentMethod)) return null;
        if (!"card".equals(paymentMethod)) return "Unsupported payment method.";

        String digits = cardNumber == null ? "" : cardNumber.replaceAll("\\D", "");
        if (digits.length() != 16) return "Invalid card number. Please enter exactly 16 digits.";
        if (cardName == null || cardName.isBlank()) return "Please enter the cardholder name.";
        if (cardExpiry == null || !cardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}")) return "Please enter expiry in MM/YY format.";
        if (cardCvv == null || !cardCvv.matches("\\d{3,4}")) return "Invalid CVV. Please enter 3 or 4 digits.";
        return null;
    }
}
