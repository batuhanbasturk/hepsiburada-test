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

    @Step("Arama kutusuna tıkla")
    public void clickSearchInput() {
        new HomePage(DriverFactory.getDriver()).clickSearchInput();
    }

    @Step("<terimKey> ürününü ara")
    public void searchProduct(String terimKey) {
        new HomePage(DriverFactory.getDriver()).typeSearchTermAndSubmit(ElementRepository.value(terimKey));
    }

    @Step("<satırKey> nolu satırdaki <ürünKey> nolu ürüne ait başlığı <veri key> olarak kaydet")
    public void captureProductTitle(String satirKey, String urunKey, String dataKey) {
        WebElement productLink = locateProduct(satirKey, urunKey);
        ScenarioDataStore.put(ElementRepository.value(dataKey), productLink.getAttribute("title"));
    }

    @Step("<satırKey> nolu satırdaki <ürünKey> nolu ürüne tıkla")
    public void clickProductAt(String satirKey, String urunKey) {
        SearchResultsPage resultsPage = new SearchResultsPage(DriverFactory.getDriver());
        WebElement productLink = locateProduct(satirKey, urunKey);

        Set<String> handlesBeforeClick = DriverFactory.currentWindowHandles();
        resultsPage.clickProduct(productLink);
        DriverFactory.switchToNewWindow(handlesBeforeClick);
    }

    private WebElement locateProduct(String satirKey, String urunKey) {
        int targetRow = Integer.parseInt(ElementRepository.value(satirKey));
        int targetProductInRow = Integer.parseInt(ElementRepository.value(urunKey));
        return new SearchResultsPage(DriverFactory.getDriver()).locateProductAt(targetRow, targetProductInRow);
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
