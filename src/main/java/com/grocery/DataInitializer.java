package com.grocery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Component 1 initializer.
 * Ensures the users.txt file exists and seeds demo users on first run.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.data.users}")
    private String usersFile;

    @Override
    public void run(String... args) throws Exception {
        seedFromClasspath(usersFile, "data/users.txt",
                "# FreshMart Users - id|name|email|password|phone|role|createdAt\n");
        System.out.println("Data file verified: users.txt OK");
    }

    private void seedFromClasspath(String targetPath, String classpathResource, String fallbackHeader) throws IOException {
        Path target = Paths.get(targetPath);
        if (!Files.exists(target)) {
            Files.createDirectories(target.getParent() == null ? Paths.get(".") : target.getParent());
            ClassPathResource resource = new ClassPathResource(classpathResource);
            if (resource.exists()) {
                try (InputStream in = resource.getInputStream()) {
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Seeded users from classpath: " + targetPath);
                    return;
                }
            }
            Files.writeString(target, fallbackHeader);
            System.out.println("Created users file: " + targetPath);
        }
    }
}
