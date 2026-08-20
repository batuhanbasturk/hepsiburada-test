package com.hepsiburada.pages;

import com.hepsiburada.config.ElementRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ElementRepository.value("EXPLICIT_WAIT_SECONDS"))));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitVisible(String key) {
        return waitVisible(ElementRepository.locator(key));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitClickable(String key) {
        return waitClickable(ElementRepository.locator(key));
    }

    protected List<WebElement> waitAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected List<WebElement> waitAllVisible(String key) {
        return waitAllVisible(ElementRepository.locator(key));
    }

    protected boolean isVisible(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isVisible(String key) {
        return isVisible(ElementRepository.locator(key));
    }

    // Locator'ı JSON repodaki key ile alan sayfalar için ortak, generic aksiyonlar;
    // spec/cpt dosyalarında element key'i doğrudan parametre olarak geçilebilsin diye.
    public void click(String key) {
        waitClickable(key).click();
    }

    public boolean isElementVisible(String key) {
        return isVisible(key);
    }

    public void enterText(String key, String value) {
        waitVisible(key).sendKeys(value);
    }

    public boolean isAnyElementVisible(String key) {
        return !waitAllVisible(key).isEmpty();
    }

    public List<WebElement> allVisible(String key) {
        return waitAllVisible(key);
    }

    public String textOf(String key) {
        return waitVisible(key).getText().trim();
    }

    // Rozet gibi sayaç elementlerinde kararlı bir data-test-id olmayabilir; rakamları
    // elementin metninden çıkarır.
    public int counterValue(String key) {
        String digits = waitVisible(key).getText().replaceAll("\\D", "");
        return digits.isBlank() ? 0 : Integer.parseInt(digits);
    }

    // Sayaç asenkron güncellenebilir (backend çağrısı + re-render sonrası); eski değeri
    // görmemek için sayaç öncekinden büyük olana kadar bekle.
    public int waitForCounterAbove(String key, int previousCount) {
        try {
            return wait.until(driver -> {
                int count = counterValue(key);
                return count > previousCount ? count : null;
            });
        } catch (TimeoutException e) {
            return counterValue(key);
        }
    }
}
