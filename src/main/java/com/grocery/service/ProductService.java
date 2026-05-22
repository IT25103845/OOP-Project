package com.grocery.service;

import com.grocery.model.Product;
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
 * ProductService handles all product-related business logic.
 * CRUD: Create, Read (all/by id/search/by category), Update, Delete
 */
@Service
public class ProductService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    private FileUtil fileUtil;

    // ===== CREATE =====
    public Product addProduct(String name, String category, double price,
                              int stock, String description, String imageEmoji) {
        String id = fileUtil.generateId(fileUtil.getProductsFilePath());
        String createdAt = LocalDateTime.now().format(FORMATTER);
        Product product = new Product(id, name, category, price, stock, description, imageEmoji, createdAt);
        fileUtil.appendLine(fileUtil.getProductsFilePath(), product.toFileString());
        return product;
    }

    // ===== READ =====
    public List<Product> getAllProducts() {
        List<String> lines = fileUtil.readLines(fileUtil.getProductsFilePath());
        List<Product> products = new ArrayList<>();
        for (String line : lines) {
            Product p = Product.fromFileString(line);
            if (p != null) products.add(p);
        }
        return products;
    }

    public Optional<Product> findById(String id) {
        return getAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> searchProducts(String query) {
        return getAllProducts().stream()
                .filter(p -> p.matchesSearch(query))
                .collect(Collectors.toList());
    }

    public List<Product> getByCategory(String category) {
        return getAllProducts().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return getAllProducts().stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // ===== UPDATE =====
    public boolean updateProduct(String id, String name, String category,
                                  double price, int stock, String description, String imageEmoji) {
        Optional<Product> existing = findById(id);
        if (existing.isEmpty()) return false;
        Product p = existing.get();
        p.setName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setStock(stock);
        p.setDescription(description);
        p.setImageEmoji(imageEmoji);
        return fileUtil.updateLineById(fileUtil.getProductsFilePath(), id, p.toFileString());
    }

    public boolean decreaseStock(String productId, int quantity) {
        Optional<Product> opt = findById(productId);
        if (opt.isEmpty()) return false;
        Product p = opt.get();
        if (p.getStock() < quantity) return false;
        p.setStock(p.getStock() - quantity);
        return fileUtil.updateLineById(fileUtil.getProductsFilePath(), productId, p.toFileString());
    }

    // ===== DELETE =====
    public boolean deleteProduct(String id) {
        return fileUtil.deleteLineById(fileUtil.getProductsFilePath(), id);
    }

    public long getTotalProducts() {
        return getAllProducts().size();
    }

    public long getLowStockCount() {
        return getAllProducts().stream().filter(p -> p.getStock() < 10).count();
    }
}
