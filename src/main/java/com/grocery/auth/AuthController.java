package com.grocery.controller;

import com.grocery.model.User;
import com.grocery.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * AuthController - Topic 1: User Authentication & Account Management
 * Handles: login, register, logout, profile view/edit
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Home page
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "index";
        }
        return "redirect:/login";
    }

    // ========== LOGIN ==========
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/";
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {
        Optional<User> userOpt = userService.login(email, password);
        if (userOpt.isPresent()) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/";
        }
        ra.addFlashAttribute("error", "Invalid email or password. Please try again.");
        return "redirect:/login";
    }

    // ========== REGISTER ==========
    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/";
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String phone,
                           RedirectAttributes ra) {
        User user = userService.register(name, email, password, phone);
        if (user != null) {
            ra.addFlashAttribute("success", "Account created! Please log in.");
            return "redirect:/login";
        }
        ra.addFlashAttribute("error", "Email already in use. Please try a different one.");
        return "redirect:/register";
    }

    // ========== LOGOUT ==========
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ========== PROFILE ==========
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "auth/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam(required = false) String newPassword,
                                HttpSession session,
                                RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        boolean success = userService.updateProfile(user.getId(), name, phone, newPassword);
        if (success) {
            // Refresh session user
            userService.findById(user.getId()).ifPresent(u -> session.setAttribute("user", u));
            ra.addFlashAttribute("success", "Profile updated successfully!");
        } else {
            ra.addFlashAttribute("error", "Update failed. Please try again.");
        }
        return "redirect:/profile";
    }

    // ========== ADMIN: USER MANAGEMENT ==========
    @GetMapping("/admin/users")
    public String adminUsers(HttpSession session, Model model,
                              @RequestParam(required = false) String search) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) return "redirect:/";
        model.addAttribute("user", user);
        model.addAttribute("users", search != null && !search.isEmpty()
                ? userService.searchUsers(search) : userService.getAllUsers());
        model.addAttribute("search", search);
        return "auth/admin-users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) return "redirect:/";
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }
}
