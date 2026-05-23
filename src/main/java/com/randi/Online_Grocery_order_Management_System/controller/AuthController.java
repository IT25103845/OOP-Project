package com.randi.Online_Grocery_order_Management_System.controller;

import com.randi.Online_Grocery_order_Management_System.model.User;
import com.randi.Online_Grocery_order_Management_System.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Minimal authentication support for Component 6: Admin Dashboard.
 * Only admin users can access the dashboard and management screens.
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (!user.isAdmin()) return "redirect:/login?adminOnly";
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {
        Optional<User> userOpt = userService.login(email, password);
        if (userOpt.isPresent() && userOpt.get().isAdmin()) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/admin/dashboard";
        }
        ra.addFlashAttribute("error", "Admin login required. Please use a valid admin account.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
