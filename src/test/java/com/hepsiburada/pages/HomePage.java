package com.hepsiburada.pages;

import com.hepsiburada.config.FrameworkConfig;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

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

    // Sayfa açılışında gelen "İzin Tercihlerinizi Özelleştirelim" çerez banner'ı
    // varsa Kabul Et'e tıkla, yoksa kısa sürede devam et.
    private void dismissCookieBannerIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(d ->
                (Boolean) ((JavascriptExecutor) d).executeScript(DISMISS_COOKIE_BANNER_SCRIPT));
        } catch (TimeoutException e) {
            // banner görünmedi, devam
        }
    }

    // Tıkladıktan sonra açılan "Popüler aramalar" panelini bekleyip
    // (bu, yeni/interaktif arama kutusunun hazır olduğunun somut işareti) öyle yazıyoruz.
    public void searchFor(String term) {
        WebElement input = waitVisible("SEARCH_INPUT");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        waitVisible("TRENDING_SEARCHES_PANEL");
        WebElement readyInput = waitVisible("SEARCH_INPUT");
        readyInput.clear();
        readyInput.sendKeys(term);
        readyInput.sendKeys(Keys.ENTER);
    }
}
