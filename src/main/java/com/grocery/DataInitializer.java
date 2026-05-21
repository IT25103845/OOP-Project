package com.grocery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Ensures the product data file exists on startup and seeds it from resources.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.data.products}")
    private String productsFile;

    @Override
    public void run(String... args) throws Exception {
        seedFromClasspath(productsFile, "data/products.txt",
                "# FreshMart Products - id|name|category|price|stock|description|imageEmoji|createdAt\n");
        System.out.println("  Product data file verified: OK");
    }

    private void seedFromClasspath(String targetPath, String classpathResource, String fallbackHeader) throws IOException {
        Path target = Paths.get(targetPath);
        if (!Files.exists(target)) {
            Path parent = target.getParent();
            if (parent != null) Files.createDirectories(parent);
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
}
