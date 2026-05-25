package com.grocery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream; 
import java.nio.file.*;
import java.nio.file.StandardCopyOption;

/**
 * DataInitializer ensures all required data files exist on startup.
 * On first run, seeds data files from the bundled classpath resources
 * so the app works correctly whether run from an IDE or as a JAR.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.data.users}")
    private String usersFile;

    @Value("${app.data.products}")
    private String productsFile;

    @Value("${app.data.orders}")
    private String ordersFile;

    @Value("${app.data.carts}")
    private String cartsFile;

    @Override
    public void run(String... args) throws Exception {
        // Seed from classpath so login works out-of-the-box from a JAR
        seedFromClasspath(usersFile,    "data/users.txt",    "# FreshMart Users - id|name|email|password|phone|role|createdAt\n");
        seedFromClasspath(productsFile, "data/products.txt", "# FreshMart Products - id|name|category|price|stock|description|imageEmoji|createdAt\n");
        ensureFileExists(ordersFile,   "# FreshMart Orders - id|userId|userName|status|total|address|createdAt|items\n");
        ensureFileExists(cartsFile,    "# FreshMart Carts - userId|items\n");
        System.out.println("  Data files verified: OK");
    }

    /**
     * If the target file doesn't exist yet, copy the seed file from the classpath.
     * Falls back to writing a blank header if the classpath resource is missing.
     */
    private void seedFromClasspath(String targetPath, String classpathResource, String fallbackHeader) throws IOException {
        Path target = Paths.get(targetPath);
        if (!Files.exists(target)) {
            Files.createDirectories(target.getParent() == null ? Paths.get(".") : target.getParent());
            ClassPathResource resource = new ClassPathResource(classpathResource);
            if (resource.exists()) {
                try (InputStream in = resource.getInputStream()) {
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("  Seeded from classpath: " + targetPath);
                    return;
                }
            }
            // Classpath resource not found — write blank header so app still starts
            Files.writeString(target, fallbackHeader);
            System.out.println("  Created (empty): " + targetPath);
        }
    }

    private void ensureFileExists(String path, String header) throws IOException {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            Files.createDirectories(p.getParent() == null ? Paths.get(".") : p.getParent());
            Files.writeString(p, header);
            System.out.println("  Created: " + path);
        }
    }
}
