package com.hepsiburada.steps;

import com.hepsiburada.config.ConfigReader;
import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.HomePage;
import com.hepsiburada.pages.LoginPage;
import com.thoughtworks.gauge.Step;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private final ConfigReader configReader = ConfigReader.fromSystemEnv();

    @Step("Hepsiburada anasayfasına git")
    public void openHomePage() {
        new HomePage(DriverFactory.getDriver()).open();
    }

    @Step("Hesap göstergesine tıkla")
    public void clickAccountIndicator() {
        new HomePage(DriverFactory.getDriver()).clickAccountIndicator();
    }

    @Step("Giriş yap butonunun göründüğünü doğrula")
    public void verifyLoginLinkVisible() {
        boolean visible = new HomePage(DriverFactory.getDriver()).isLoginLinkVisible();
        assertThat(visible)
            .as("Beklenen: giriş yap butonu görünür olmalı")
            .isTrue();
    }

    @Step("Giriş yap linkine tıkla")
    public void clickLoginLink() {
        new HomePage(DriverFactory.getDriver()).clickLoginLink();
    }

    @Step("Giriş sayfasına yönlendiğini doğrula")
    public void verifyOnLoginPage() {
        boolean loaded = new LoginPage(DriverFactory.getDriver()).isLoaded();
        assertThat(loaded)
            .as("Beklenen: giriş sayfasına yönlendirilmiş olmalı")
            .isTrue();
    }

    @Step("E-posta adresini gir")
    public void enterEmail() {
        new LoginPage(DriverFactory.getDriver()).enterEmail(configReader.getUsername());
    }

    @Step("Şifreyi gir")
    public void enterPassword() {
        new LoginPage(DriverFactory.getDriver()).enterPassword(configReader.getPassword());
    }

    @Step("Giriş yap butonuna tıkla")
    public void clickLoginButton() {
        new LoginPage(DriverFactory.getDriver()).clickLoginButton();
    }

    @Step("Kullanıcının giriş yaptığını doğrula")
    public void verifyLoggedIn() {
        boolean loggedIn = new HomePage(DriverFactory.getDriver()).isLoggedIn();
        assertThat(loggedIn)
            .as("Beklenen: kullanıcı giriş yapmış olmalı")
            .isTrue();
    }
}
