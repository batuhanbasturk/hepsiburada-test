package com.hepsiburada.steps;

import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.CartPage;
import com.hepsiburada.pages.HomePage;
import com.hepsiburada.pages.ProductPage;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.ScenarioDataStore;

import static org.assertj.core.api.Assertions.assertThat;

public class CartSteps {

    @Step("Ürünü sepete ekle")
    public void addToCart() {
        int countBefore = new HomePage(DriverFactory.getDriver()).cartCountAsInt();
        ScenarioDataStore.put(ScenarioDataKeys.CART_COUNT_BEFORE_ADD, countBefore);

        new ProductPage(DriverFactory.getDriver()).addToCart();
    }

    // Sepete eklemede toast/snackbar yok; header'daki sayaç asenkron güncelleniyor,
    // bu yüzden statik ">0" yerine önceki değerden büyük olana kadar bekliyoruz.
    @Step("Sepete eklendiğini doğrula")
    public void verifyAddedToCart() {
        int countBefore = (int) ScenarioDataStore.get(ScenarioDataKeys.CART_COUNT_BEFORE_ADD);
        int countAfter = new HomePage(DriverFactory.getDriver()).waitForCartCountAbove(countBefore);

        assertThat(countAfter)
            .as("Beklenen: sepet sayacı sepete ekleme sonrası artmalı")
            .isGreaterThan(countBefore);
    }

    @Step("Sepeti aç")
    public void openCart() {
        new HomePage(DriverFactory.getDriver()).openCart();
    }

    @Step("Sepette ürünün göründüğünü doğrula")
    public void verifyProductInCart() {
        String expectedTitle = (String) ScenarioDataStore.get(ScenarioDataKeys.CAPTURED_TITLE);
        boolean present = new CartPage(DriverFactory.getDriver()).containsProduct(expectedTitle);

        assertThat(present)
            .as("Beklenen: sepette eklenen ürün görünmeli")
            .isTrue();
    }
}
