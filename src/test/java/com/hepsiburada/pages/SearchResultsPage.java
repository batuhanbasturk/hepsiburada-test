package com.hepsiburada.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsPage extends BasePage {

    // [id]: aynı class prefix'ini paylaşan ama gerçek ürün kartı olmayan
    // sponsorlu/banner <li> elemanlarını (id'si yok) eler.
    private static final By PRODUCT_CARDS = By.cssSelector("li[class*='productListContent'][id]");
    private static final By PRODUCT_LINK = By.cssSelector("a[class*='productCardLink']");

    // Sonuç grid'i sabit 4 sütunlu (grid-template-columns: 226px x4); satır/sütun bu yüzden
    // düz index aritmetiğiyle bulunabiliyor. Her kartın id'si de zaten "i{index}" (sponsorlu
    // kartlar hariç, sıralı) olduğundan hesaplanan index'e doğrudan By.id ile gidilebiliyor.
    private static final int COLUMNS_PER_ROW = 4;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasResults() {
        return !waitAllVisible(PRODUCT_CARDS).isEmpty();
    }

    public WebElement locateProductAt(int targetRow, int targetProductInRow) {
        int index = (targetRow - 1) * COLUMNS_PER_ROW + (targetProductInRow - 1);
        return waitVisible(By.id("i" + index)).findElement(PRODUCT_LINK);
    }

    public void clickProduct(WebElement productLink) {
        productLink.click();
    }
}
