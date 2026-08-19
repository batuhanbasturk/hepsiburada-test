package com.hepsiburada.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private static final By CART_ITEM_NAME = By.cssSelector("div[class*='product_name'] a");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean containsProduct(String expectedTitle) {
        return waitAllVisible(CART_ITEM_NAME).stream()
            .anyMatch(el -> el.getText().trim().contains(expectedTitle));
    }
}
