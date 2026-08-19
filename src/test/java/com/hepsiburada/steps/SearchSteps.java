package com.hepsiburada.steps;

import com.hepsiburada.driver.DriverFactory;
import com.hepsiburada.pages.HomePage;
import com.hepsiburada.pages.ProductPage;
import com.hepsiburada.pages.SearchResultsPage;
import com.hepsiburada.testdata.SearchTestData;
import com.hepsiburada.testdata.TestDataReader;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.ScenarioDataStore;
import org.openqa.selenium.WebElement;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchSteps {

    private final TestDataReader testDataReader = new TestDataReader();

    @Step("Ürünü ara")
    public void searchProduct() {
        SearchTestData data = testDataReader.readSearchTestData("testdata/search_testdata.json");
        new HomePage(DriverFactory.getDriver()).searchFor(data.getSearchTerm());
    }

    @Step("Arama sonuçlarının geldiğini doğrula")
    public void verifySearchResults() {
        boolean hasResults = new SearchResultsPage(DriverFactory.getDriver()).hasResults();
        assertThat(hasResults)
            .as("Beklenen: arama sonuçları listelenmiş olmalı")
            .isTrue();
    }

    @Step("İkinci satırdaki ilk ürüne tıkla")
    public void clickSecondRowFirstProduct() {
        SearchTestData data = testDataReader.readSearchTestData("testdata/search_testdata.json");
        SearchResultsPage resultsPage = new SearchResultsPage(DriverFactory.getDriver());

        WebElement productLink = resultsPage.locateProductAt(data.getTargetRow(), data.getTargetProductInRow());
        String expectedTitle = productLink.getAttribute("title");
        ScenarioDataStore.put(ScenarioDataKeys.CAPTURED_TITLE, expectedTitle);

        Set<String> handlesBeforeClick = DriverFactory.currentWindowHandles();
        resultsPage.clickProduct(productLink);
        DriverFactory.switchToNewWindow(handlesBeforeClick);
    }

    @Step("Ürün sayfasına yönlendiğini doğrula")
    public void verifyOnProductPage() {
        String expectedTitle = (String) ScenarioDataStore.get(ScenarioDataKeys.CAPTURED_TITLE);
        String actualTitle = new ProductPage(DriverFactory.getDriver()).getTitle();

        assertThat(actualTitle)
            .as("Beklenen: ürün sayfası başlığı arama sonucundaki ürünle eşleşmeli")
            .isEqualTo(expectedTitle);
    }
}
