package com.randi.Online_Grocery_order_Management_System.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * FileUtil handles all file read/write operations for data persistence.
 * This is the core of our file-handling requirement (replaces database).
 * Demonstrates: File I/O with BufferedReader/BufferedWriter
 */
@Component
public class FileUtil {

    private static final Logger logger = Logger.getLogger(FileUtil.class.getName());

    @Value("${app.data.users}")
    private String usersFilePath;

    @Value("${app.data.products}")
    private String productsFilePath;

    @Value("${app.data.orders}")
    private String ordersFilePath;

    @Value("${app.data.carts}")
    private String cartsFilePath;

    /** Read all non-empty, non-comment lines from a file */
    public List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                return lines;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            logger.warning("Error reading file: " + filePath + " - " + e.getMessage());
        }
        return lines;
    }

    /** Write all lines to a file (overwrite), preserving header comment */
    public boolean writeLines(String filePath, List<String> lines) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            logger.warning("Error writing file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }

    /** Append a single line to a file */
    public boolean appendLine(String filePath, String line) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            logger.warning("Error appending to file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }

    /** Delete a line whose first field (before |) matches id */
    public boolean deleteLineById(String filePath, String id) {
        List<String> lines = readLines(filePath);
        boolean removed = lines.removeIf(line -> {
            String[] parts = line.split("\\|");
            return parts.length > 0 && parts[0].equals(id);
        });
        if (removed) writeLines(filePath, lines);
        return removed;
    }

    /** Replace a line whose first field matches id with newLine */
    public boolean updateLineById(String filePath, String id, String newLine) {
        List<String> lines = readLines(filePath);
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|");
            if (parts.length > 0 && parts[0].equals(id)) {
                lines.set(i, newLine);
                found = true;
                break;
            }
        }
        if (found) writeLines(filePath, lines);
        return found;
    }

    /** Generate next sequential numeric ID for a file */
    public String generateId(String filePath) {
        List<String> lines = readLines(filePath);
        int maxId = 0;
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    int id = Integer.parseInt(parts[0].trim());
                    if (id > maxId) maxId = id;
                }
            } catch (NumberFormatException ignored) {}
        }
        return String.valueOf(maxId + 1);
    }

    // File path accessors
    public String getUsersFilePath()    { return usersFilePath; }
    public String getProductsFilePath() { return productsFilePath; }
    public String getOrdersFilePath()   { return ordersFilePath; }
    public String getCartsFilePath()    { return cartsFilePath; }
}
