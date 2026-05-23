package com.grocery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;

@Component
public class DataInitializer implements CommandLineRunner {
    @Value("${app.data.users}") private String usersFile;
    @Value("${app.data.orders}") private String ordersFile;
    @Value("${app.data.products}") private String productsFile;
    @Value("${app.data.carts}") private String cartsFile;

    @Override
    public void run(String... args) throws Exception {
        seed(usersFile, "data/users.txt", "# FreshMart Users - id|name|email|password|phone|role|createdAt\n");
        seed(ordersFile, "data/orders.txt", "# FreshMart Orders - id|userId|userName|status|total|address|createdAt|items\n");
        ensure(productsFile, "# Not used by Component 4 - Order Management\n");
        ensure(cartsFile, "# Not used by Component 4 - Order Management\n");
        System.out.println("  Data files verified: OK");
    }

    private void seed(String targetPath, String classpathResource, String fallbackHeader) throws Exception {
        Path target = Paths.get(targetPath);
        if (!Files.exists(target)) {
            Files.createDirectories(target.getParent() == null ? Paths.get(".") : target.getParent());
            ClassPathResource resource = new ClassPathResource(classpathResource);
            if (resource.exists()) {
                try (InputStream in = resource.getInputStream()) {
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                    return;
                }
            }
            Files.writeString(target, fallbackHeader);
        }
    }

    private void ensure(String path, String header) throws Exception {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            Files.createDirectories(p.getParent() == null ? Paths.get(".") : p.getParent());
            Files.writeString(p, header);
        }
    }
}
