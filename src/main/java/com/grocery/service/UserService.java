package com.grocery.service;

import com.grocery.model.User;
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
 * UserService handles all user-related business logic.
 * Demonstrates OOP: Service layer encapsulation
 * CRUD: Create (register), Read (login/profile), Update (profile), Delete (account)
 */
@Service
public class UserService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private FileUtil fileUtil;

    // ===== CREATE =====
    public User register(String name, String email, String password, String phone) {
        if (findByEmail(email).isPresent()) {
            return null; // Email already exists
        }
        String id = fileUtil.generateId(fileUtil.getUsersFilePath());
        String createdAt = LocalDateTime.now().format(FORMATTER);
        User user = new User(id, name, email, password, phone, "CUSTOMER", createdAt);
        fileUtil.appendLine(fileUtil.getUsersFilePath(), user.toFileString());
        return user;
    }

    // ===== READ =====
    public List<User> getAllUsers() {
        List<String> lines = fileUtil.readLines(fileUtil.getUsersFilePath());
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            User u = User.fromFileString(line);
            if (u != null) users.add(u);
        }
        return users;
    }

    public Optional<User> findById(String id) {
        return getAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return getAllUsers().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Optional<User> login(String email, String password) {
        return getAllUsers().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && u.getPassword().equals(password))
                .findFirst();
    }

    public List<User> searchUsers(String query) {
        return getAllUsers().stream()
                .filter(u -> u.matchesSearch(query))
                .collect(Collectors.toList());
    }

    // ===== UPDATE =====
    public boolean updateProfile(String id, String name, String phone, String newPassword) {
        Optional<User> existing = findById(id);
        if (existing.isEmpty()) return false;
        User user = existing.get();
        user.setName(name);
        user.setPhone(phone);
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(newPassword);
        }
        return fileUtil.updateLineById(fileUtil.getUsersFilePath(), id, user.toFileString());
    }

    // ===== DELETE =====
    public boolean deleteUser(String id) {
        return fileUtil.deleteLineById(fileUtil.getUsersFilePath(), id);
    }

    public long getTotalCustomers() {
        return getAllUsers().stream().filter(u -> !u.isAdmin()).count();
    }
}
