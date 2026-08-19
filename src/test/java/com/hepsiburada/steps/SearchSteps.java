package com.hepsiburada.steps;

import com.hepsiburada.config.ElementRepository;
import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.HomePage;
import com.hepsiburada.pages.ProductPage;
import com.hepsiburada.pages.SearchResultsPage;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.ScenarioDataStore;
import org.openqa.selenium.WebElement;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchSteps {

    @Step("<terimKey> ürününü ara")
    public void searchProduct(String terimKey) {
        new HomePage(DriverFactory.getDriver()).searchFor(ElementRepository.value(terimKey));
    }

    @Step("<satırKey> nolu satırdaki <ürünKey> nolu ürüne tıklayıp başlığını <veri key> olarak kaydet")
    public void clickProductAt(String satirKey, String urunKey, String dataKey) {
        int targetRow = Integer.parseInt(ElementRepository.value(satirKey));
        int targetProductInRow = Integer.parseInt(ElementRepository.value(urunKey));
        SearchResultsPage resultsPage = new SearchResultsPage(DriverFactory.getDriver());

        WebElement productLink = resultsPage.locateProductAt(targetRow, targetProductInRow);
        String expectedTitle = productLink.getAttribute("title");
        ScenarioDataStore.put(ElementRepository.value(dataKey), expectedTitle);

        Set<String> handlesBeforeClick = DriverFactory.currentWindowHandles();
        resultsPage.clickProduct(productLink);
        DriverFactory.switchToNewWindow(handlesBeforeClick);
    }

    @Step("<key> elementinin metni <veri key> ile eşleştiğini doğrula")
    public void verifyElementTextMatchesStoredValue(String key, String dataKey) {
        String expectedTitle = (String) ScenarioDataStore.get(ElementRepository.value(dataKey));
        String actualTitle = new ProductPage(DriverFactory.getDriver()).textOf(key);

        assertThat(actualTitle)
            .as("Beklenen: '" + key + "' elementinin metni '" + dataKey + "' ile eşleşmeli")
            .isEqualTo(expectedTitle);
    }
}
