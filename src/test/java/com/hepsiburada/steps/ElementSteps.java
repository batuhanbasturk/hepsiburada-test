package com.hepsiburada.steps;

import com.hepsiburada.config.ElementRepository;
import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.HomePage;
import com.thoughtworks.gauge.Step;

import static org.assertj.core.api.Assertions.assertThat;

// config/elements.json'daki key'lerle çalışan, sayfadan bağımsız genel element aksiyonları.
// Bu step'ler login/search/cart concept'lerinin hepsinden aynı metinle, farklı key
// parametreleriyle çağrılabiliyor; yeni bir element için yeni step yazmaya gerek kalmıyor.
public class ElementSteps {

    @Step("<key> adresine git")
    public void navigateTo(String key) {
        DriverFactory.getDriver().get(ElementRepository.value(key));
    }

    @Step("<hostKey> içindeki <targetKey> elementine varsa tıkla")
    public void clickShadowElementIfPresent(String hostKey, String targetKey) {
        new HomePage(DriverFactory.getDriver()).clickShadowElementIfPresent(hostKey, targetKey);
    }

    @Step("<key> elementine tıkla")
    public void clickElement(String key) {
        new HomePage(DriverFactory.getDriver()).click(key);
    }

    @Step("<key> elementinin göründüğünü doğrula")
    public void verifyElementVisible(String key) {
        boolean visible = new HomePage(DriverFactory.getDriver()).isElementVisible(key);
        assertThat(visible)
            .as("Beklenen: '" + key + "' elementi görünür olmalı")
            .isTrue();
    }

    @Step("<key> elementlerinin göründüğünü doğrula")
    public void verifyElementsVisible(String key) {
        boolean visible = new HomePage(DriverFactory.getDriver()).isAnyElementVisible(key);
        assertThat(visible)
            .as("Beklenen: '" + key + "' elementlerinden en az biri görünür olmalı")
            .isTrue();
    }

    // Test hesabı bulunmadığından (yalnızca misafir oturumu ile doğrulandı) giriş başarısı,
    // hesap göstergesi gibi elementlerin artık misafir metnini göstermediğini kontrol ederek
    // dolaylı biçimde doğrulanıyor.
    @Step("<key> elementinin metni <beklenmeyen metin> olmamalı")
    public void verifyElementTextIsNot(String key, String unexpectedText) {
        HomePage page = new HomePage(DriverFactory.getDriver());
        boolean ok = page.isElementVisible(key) && !page.textOf(key).equalsIgnoreCase(unexpectedText);
        assertThat(ok)
            .as("Beklenen: '" + key + "' elementinin metni '" + unexpectedText + "' olmamalı")
            .isTrue();
    }
}
