package com.hepsiburada.pages;

import com.hepsiburada.config.ElementRepository;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;

import java.util.List;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Sayfa açılışında gelen "İzin Tercihlerinizi Özelleştirelim" çerez banner'ı
    // varsa Kabul Et'e tıkla, yoksa kısa sürede devam et.
    public void clickShadowElementIfPresent(String hostKey, String targetKey) {
        try {
            WebElement target = wait.until(d -> {
                List<WebElement> hosts = d.findElements(ElementRepository.locator(hostKey));
                if (hosts.isEmpty()) {
                    return null;
                }
                SearchContext shadowRoot = hosts.get(0).getShadowRoot();
                List<WebElement> targets = shadowRoot.findElements(ElementRepository.locator(targetKey));
                return targets.isEmpty() ? null : targets.get(0);
            });
            target.click();
        } catch (TimeoutException e) {
            // element görünmedi, devam
        }
    }

    public void clickSearchInput() {
        WebElement input = waitVisible("SEARCH_INPUT");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
    }

    public void typeSearchTermAndSubmit(String term) {
        WebElement readyInput = waitVisible("SEARCH_INPUT");
        readyInput.clear();
        readyInput.sendKeys(term);
        readyInput.sendKeys(Keys.ENTER);
    }
}
