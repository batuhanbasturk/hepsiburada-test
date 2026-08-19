package com.hepsiburada.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {

    private static final By TITLE = By.tagName("h1");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector("[data-test-id='addToCart']");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return waitVisible(TITLE).getText().trim();
    }

    public void addToCart() {
        waitClickable(ADD_TO_CART_BUTTON).click();
    }
}
