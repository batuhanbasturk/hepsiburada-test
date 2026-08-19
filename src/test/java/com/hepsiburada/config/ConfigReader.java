package com.hepsiburada.config;

import java.util.Map;

public class ConfigReader {

    private final Map<String, String> source;

    public ConfigReader(Map<String, String> source) {
        this.source = source;
    }

    public static ConfigReader fromSystemEnv() {
        return new ConfigReader(System.getenv());
    }

    public String getUsername() {
        return require("HB_USERNAME");
    }

    public String getPassword() {
        return require("HB_PASSWORD");
    }

    private String require(String key) {
        String value = source.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                "Required environment variable '" + key + "' is not set. " +
                "Set it before running the suite, e.g. HB_USERNAME=you@example.com");
        }
        return value;
    }
}
