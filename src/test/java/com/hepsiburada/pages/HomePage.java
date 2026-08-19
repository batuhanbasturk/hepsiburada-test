package com.hepsiburada.pages;

import com.hepsiburada.config.FrameworkConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    private static final By SEARCH_INPUT = By.cssSelector("input[data-test-id='search-bar-input']");
    private static final By ACCOUNT_INDICATOR = By.cssSelector("[data-test-id='account']");
    private static final By CART_LINK = By.cssSelector("a[href*='sepetim']");
    private static final By TRENDING_SEARCHES_PANEL = By.cssSelector("[class*='trendingTerms']");

    // "Kabul Et" butonu <efilli-layout-dynamic> elementinin shadow DOM'u içinde
    // (<template shadowrootmode="open">); normal By.id/cssSelector shadow root'u
    // göremediği için bulunamıyordu, bu yüzden JS ile shadow root'a inip tıklıyoruz.
    private static final String DISMISS_COOKIE_BANNER_SCRIPT =
        "var host = document.querySelector('efilli-layout-dynamic');" +
        "if (!host || !host.shadowRoot) return false;" +
        "var btn = host.shadowRoot.getElementById('hb-accept-all');" +
        "if (!btn) return false;" +
        "btn.click();" +
        "return true;";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(FrameworkConfig.baseUrl());
        dismissCookieBannerIfPresent();
    }

    // Sayfa açılışında gelen "İzin Tercihlerinizi Özelleştirelim" çerez banner'ı sonraki
    // tıklama/yazmaların üzerine binip "not interactable" hatasına yol açabiliyor;
    // varsa Kabul Et'e tıkla, yoksa kısa sürede devam et.
    private void dismissCookieBannerIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(d ->
                (Boolean) ((JavascriptExecutor) d).executeScript(DISMISS_COOKIE_BANNER_SCRIPT));
        } catch (TimeoutException e) {
            // banner görünmedi, devam
        }
    }

    public LoginPage goToLogin() {
        waitClickable(ACCOUNT_INDICATOR).click();
        waitClickable(By.id("login")).click();
        return new LoginPage(driver);
    }

    // Selector incelemesi sırasında test hesabı olmadığından (sadece misafir oturumu),
    // hesap göstergesinin artık misafir "Giriş Yap" metnini göstermediğini kontrol eder.
    public boolean isLoggedIn() {
        return isVisible(ACCOUNT_INDICATOR)
            && !waitVisible(ACCOUNT_INDICATOR).getText().trim().equalsIgnoreCase("Giriş Yap");
    }

    // Header'da arama kutusunun eski (statik) ve yeni (interaktif) iki varyantı var; hangisi
    // DOM'da geliyorsa ona tıklamak eskiyse client-side'da yeniye "swap" ediyor, bu swap'ın
    // zamanlaması garanti değil. Tıkladıktan sonra açılan "Popüler aramalar" panelini bekleyip
    // (bu, yeni/interaktif arama kutusunun hazır olduğunun somut işareti) öyle yazıyoruz.
    public void searchFor(String term) {
        WebElement input = waitVisible(SEARCH_INPUT);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        waitVisible(TRENDING_SEARCHES_PANEL);
        WebElement readyInput = waitVisible(SEARCH_INPUT);
        readyInput.clear();
        readyInput.sendKeys(term);
        readyInput.sendKeys(Keys.ENTER);
    }

    public void openCart() {
        waitClickable(CART_LINK).click();
    }

    // Sepet rozetinde kararlı bir data-test-id yok; rakamları sepet linkinin metninden çıkar.
    public String cartCount() {
        String text = waitVisible(CART_LINK).getText();
        return text.replaceAll("\\D", "");
    }

    public int cartCountAsInt() {
        String digits = cartCount();
        return digits.isBlank() ? 0 : Integer.parseInt(digits);
    }

    // Sepete ekleme rozeti asenkron günceller (backend çağrısı + re-render);
    // eski değeri görmemek için sayaç öncekinden büyük olana kadar bekle.
    public int waitForCartCountAbove(int previousCount) {
        try {
            return wait.until(driver -> {
                int count = cartCountAsInt();
                return count > previousCount ? count : null;
            });
        } catch (TimeoutException e) {
            return cartCountAsInt();
        }
    }
}
