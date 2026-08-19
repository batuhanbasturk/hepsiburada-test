package com.hepsiburada.pages;

import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean containsProduct(String key, String expectedTitle) {
        return allVisible(key).stream()
            .anyMatch(el -> el.getText().trim().contains(expectedTitle));
    }
}
