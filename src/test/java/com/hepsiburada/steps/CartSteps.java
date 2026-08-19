package com.hepsiburada.steps;

import com.hepsiburada.config.ElementRepository;
import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.CartPage;
import com.hepsiburada.pages.HomePage;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.ScenarioDataStore;

import static org.assertj.core.api.Assertions.assertThat;

public class CartSteps {

    @Step("<key> elementinin sayacını <veri key> olarak kaydet")
    public void captureCounter(String key, String dataKey) {
        int count = new HomePage(DriverFactory.getDriver()).counterValue(key);
        ScenarioDataStore.put(ElementRepository.value(dataKey), count);
    }

    // Sepete eklemede toast/snackbar yok; header'daki sayaç asenkron güncelleniyor,
    // bu yüzden statik ">0" yerine önceki değerden büyük olana kadar bekliyoruz.
    @Step("<key> elementinin sayacının <veri key> değerinden büyük olduğunu doğrula")
    public void verifyCounterIncreased(String key, String dataKey) {
        int countBefore = (int) ScenarioDataStore.get(ElementRepository.value(dataKey));
        int countAfter = new HomePage(DriverFactory.getDriver()).waitForCounterAbove(key, countBefore);

        assertThat(countAfter)
            .as("Beklenen: '" + key + "' elementinin sayacı artmalı")
            .isGreaterThan(countBefore);
    }

    @Step("<key> elementlerinden birinin metni <veri key> ile eşleştiğini doğrula")
    public void verifyAnyElementTextMatchesStoredValue(String key, String dataKey) {
        String expectedTitle = (String) ScenarioDataStore.get(ElementRepository.value(dataKey));
        boolean present = new CartPage(DriverFactory.getDriver()).containsProduct(key, expectedTitle);

        assertThat(present)
            .as("Beklenen: '" + key + "' elementlerinden biri '" + dataKey + "' ile eşleşen ürünü göstermeli")
            .isTrue();
    }
}
