package com.hepsiburada.steps;

import com.hepsiburada.config.ConfigReader;
import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.HomePage;
import com.thoughtworks.gauge.Step;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private final ConfigReader configReader = ConfigReader.fromSystemEnv();

    @Step("Hepsiburada anasayfasına git")
    public void openHomePage() {
        new HomePage(DriverFactory.getDriver()).open();
    }

    @Step("Giriş yap")
    public void login() {
        new HomePage(DriverFactory.getDriver())
            .goToLogin()
            .login(configReader.getUsername(), configReader.getPassword());
    }

    @Step("Kullanıcının giriş yaptığını doğrula")
    public void verifyLoggedIn() {
        boolean loggedIn = new HomePage(DriverFactory.getDriver()).isLoggedIn();
        assertThat(loggedIn)
            .as("Beklenen: kullanıcı giriş yapmış olmalı")
            .isTrue();
    }
}
