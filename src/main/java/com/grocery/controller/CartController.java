package com.grocery.controller;

import com.grocery.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** 

 * Component 3: Shopping Cart System.
 * Handles cart viewing, adding products, updating quantities, removing items and clearing the cart.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    private static final String DEMO_USER_ID = "demo-user";

    @Autowired
    private CartService cartService;

    private String getUserId(HttpSession session) {
        Object userId = session.getAttribute("cartUserId");
        if (userId == null) {
            session.setAttribute("cartUserId", DEMO_USER_ID);
            return DEMO_USER_ID;
        }
        return userId.toString();
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(getUserId(session)));
        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        boolean added = cartService.addToCart(getUserId(session), productId, quantity);
        if (added) {
            redirectAttributes.addFlashAttribute("success", "Item added to cart!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Could not add item. Please check stock and quantity.");
        }
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam String productId,
                                 @RequestParam int quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(getUserId(session), productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Cart updated.");
        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable String productId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(getUserId(session), productId);
        redirectAttributes.addFlashAttribute("success", "Item removed from cart.");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        cartService.clearCart(getUserId(session));
        redirectAttributes.addFlashAttribute("success", "Cart cleared.");
        return "redirect:/cart";
    }
}
