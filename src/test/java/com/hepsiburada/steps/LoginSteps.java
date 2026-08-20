package com.hepsiburada.steps;

import com.hepsiburada.config.ElementRepository;
import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.HomePage;
import com.hepsiburada.pages.LoginPage;
import com.thoughtworks.gauge.Step;

public class LoginSteps {

    @Step("<key> alanına e-posta adresini gir")
    public void enterEmail(String key) {
        new LoginPage(DriverFactory.getDriver()).enterText(key, ElementRepository.value("USERNAME"));
    }

    @Step("<key> alanına şifreyi gir")
    public void enterPassword(String key) {
        new LoginPage(DriverFactory.getDriver()).enterText(key, ElementRepository.value("PASSWORD"));
    }
}
