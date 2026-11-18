package com.sdet.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * SINGLETON PATTERN:
 * Ensures config.properties is read from disk ONLY ONCE across the entire test execution.
 */
public class ConfigReader {

    private static volatile ConfigReader instance;
    private final Properties properties;

    // Private constructor prevents external instantiation via "new ConfigReader()"
    private ConfigReader() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load src/test/resources/config.properties", e);
        }
    }

    // Thread-safe Double-Checked Locking Singleton getter
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        // Allows overriding via Maven CLI (e.g., mvn test -Dbrowser=firefox)
        return System.getProperty(key, properties.getProperty(key));
    }
}