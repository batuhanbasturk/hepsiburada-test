package com.hepsiburada.config;

public final class FrameworkConfig {

    private FrameworkConfig() {
    }

    public static String baseUrl() {
        return ElementRepository.value("websiteURL");
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(System.getProperty("hb.explicit.wait.seconds", "5"));
    }
}
