package com.hepsiburada.pages;

import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void addToCart(String key) {
        click(key);
    }
}
