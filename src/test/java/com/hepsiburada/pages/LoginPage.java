package com.hepsiburada.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("txtUserName");
    private static final By PASSWORD_INPUT = By.id("txtPassword");
    private static final By LOGIN_BUTTON = By.id("btnLogin");
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isVisible(USERNAME_INPUT);
    }

    public void enterEmail(String email) {
        waitVisible(USERNAME_INPUT).sendKeys(email);
    }

    public void enterPassword(String password) {
        waitVisible(PASSWORD_INPUT).sendKeys(password);
    }

    public HomePage clickLoginButton() {
        waitClickable(LOGIN_BUTTON).click();
        return new HomePage(driver);
    }
}
