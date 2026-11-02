package com.casestudy.config;

import java.io.IOException;
import java.util.Properties;

public final class TestConfig {
    private static final Properties VALUES = new Properties();
    static {
        try (var stream = TestConfig.class.getResourceAsStream("/config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Missing config.properties");
            }
            VALUES.load(stream);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private TestConfig() {}
    public static String get(String key) {
        String value = System.getProperty(key, VALUES.getProperty(key));
        if (value == null) {
            throw new IllegalArgumentException("Missing config: " + key);
        }
        return value;
    }
}
