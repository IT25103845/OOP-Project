package com.grocery.service;

import com.grocery.model.Cart;
import com.grocery.model.Product;
import com.grocery.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * CartService handles shopping cart operations.
 * CRUD: Create cart, Read cart, Update (add/remove/change qty), Delete (clear)
 */
@Service
public class CartService {

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ProductService productService;

    public Cart getCart(String userId) {

        
        List<String> lines = fileUtil.readLines(fileUtil.getCartsFilePath());
        for (String line : lines) {
            if (line.startsWith(userId + "|")) {
                return Cart.fromFileString(line);
            }
        }
        return new Cart(userId);
    }

    private void saveCart(Cart cart) {
        List<String> lines = fileUtil.readLines(fileUtil.getCartsFilePath());
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(cart.getUserId() + "|")) {
                lines.set(i, cart.toFileString());
                found = true;
                break;
            }
        }
        if (!found) lines.add(cart.toFileString());
        fileUtil.writeLines(fileUtil.getCartsFilePath(), lines);
    }

    public boolean addToCart(String userId, String productId, int quantity) {
        Optional<Product> productOpt = productService.findById(productId);
        if (productOpt.isEmpty() || productOpt.get().getStock() < quantity) return false;
        Cart cart = getCart(userId);
        cart.addItem(productId, productOpt.get().getName(),
                productOpt.get().getPrice(), quantity);
        saveCart(cart);
        return true;
    }

    public void removeFromCart(String userId, String productId) {
        Cart cart = getCart(userId);
        cart.removeItem(productId);
        saveCart(cart);
    }

    public void updateQuantity(String userId, String productId, int qty) {
        Cart cart = getCart(userId);
        cart.updateQuantity(productId, qty);
        saveCart(cart);
    }

    public void clearCart(String userId) {
        Cart cart = getCart(userId);
        cart.clear();
        saveCart(cart);
    }

    public int getCartCount(String userId) {
        return getCart(userId).getItemCount();
    }
}
