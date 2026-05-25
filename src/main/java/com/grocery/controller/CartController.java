package com.grocery.controller;

import com.grocery.model.User;
import com.grocery.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("cart", cartService.getCart(user.getId()));
        return "cart/view";
    }

    @PostMapping("/cart/demo-fill")
    public String fillDemoCart(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        cartService.addToCart(user.getId(), "1", 2);
        cartService.addToCart(user.getId(), "3", 1);
        redirectAttributes.addFlashAttribute("success", "Demo cart loaded for checkout testing.");
        return "redirect:/cart";
    }
}
