package com.hepsiburada.pages;

import com.hepsiburada.config.ElementRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsPage extends BasePage {

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public WebElement locateProductAt(int targetRow, int targetProductInRow) {
        int columnsPerRow = Integer.parseInt(ElementRepository.value("COLUMNS_PER_ROW"));
        int index = (targetRow - 1) * columnsPerRow + (targetProductInRow - 1);
        return waitVisible(By.id("i" + index)).findElement(ElementRepository.locator("PRODUCT_LINK"));
    }

    public void clickProduct(WebElement productLink) {
        productLink.click();
    }
}
