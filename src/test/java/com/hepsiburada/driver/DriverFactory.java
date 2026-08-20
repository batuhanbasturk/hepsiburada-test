package com.hepsiburada.driver;

import com.hepsiburada.config.ElementRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Sayfa DOM'unun dışında render edilip tıklama/tuş girişlerini yutabilen
        // Chrome native uyarılarını (şifre sızıntısı, şifre kaydetme, bildirim izni) kapat.
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        DRIVER.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been created for this thread yet");
        }
        return driver;
    }

    public static Set<String> currentWindowHandles() {
        return getDriver().getWindowHandles();
    }

    // hepsiburada.com ürün sayfalarını yeni sekmede açıyor (target="_blank");
    // çağıran taraf tıklamadan önce currentWindowHandles() alıp burada yeni sekmeyi bulur.
    public static void switchToNewWindow(Set<String> handlesBeforeAction) {
        WebDriver driver = getDriver();
        new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ElementRepository.value("EXPLICIT_WAIT_SECONDS"))))
            .until(ExpectedConditions.numberOfWindowsToBe(handlesBeforeAction.size() + 1));

        Set<String> handlesAfter = driver.getWindowHandles();
        handlesAfter.removeAll(handlesBeforeAction);
        if (handlesAfter.isEmpty()) {
            throw new IllegalStateException("Expected a new browser tab/window to open, but none did");
        }
        driver.switchTo().window(handlesAfter.iterator().next());
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}
