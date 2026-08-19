package com.hepsiburada.config;

public final class FrameworkConfig {

    private FrameworkConfig() {
    }

    public static String baseUrl() {
        return System.getProperty("hb.base.url", "https://www.hepsiburada.com");
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(System.getProperty("hb.explicit.wait.seconds", "15"));
    }
}
