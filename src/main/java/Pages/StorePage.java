package Pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;


import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorePage {
   // @FindBy(id = "twotabsearchtextbox")
   // WebElement searchBox;
    By SearchBox = By.id("twotabsearchtextbox");

   // @FindBy(xpath = "//a[normalize-space()='Sneakers']")
   // WebElement sneakersFilterButton;
    By sneakersFilterButton = By.xpath("//a[normalize-space()='Sneakers']");

  //  @FindBy(id = "low-price")
  //  WebElement minPriceFilter;
    By minPriceFilter = By.id("low-price");

   // @FindBy(id = "a-autoid-1")
    //WebElement goButton;
    By goButton = By.id("a-autoid-1");

   // @FindBy(xpath = "//*[contains(@data-component-type,'s-search-result')]")
    List<SelenideElement> allSneakers = $$(By.xpath("//*[contains(@data-component-type,'s-search-result')]"));

    public Map<String, String> getAllDistinctProducts() {
        Map<String, String> result = new HashMap<>();
        for (WebElement el : allSneakers) {

            String name = el.findElement(By.cssSelector(".a-size-base-plus.a-color-base.a-text-normal")).getText();
            try{ String listOfPrices = el.findElement(By.cssSelector(".a-row.a-size-base.a-color-base")).getText();
                result.put(name, listOfPrices);
            }
            catch(Exception ex){
                result.put(name, "$0.00");
            }
        }
        System.out.println(result.keySet());
        System.out.println(result.values());
        return result;
    }

    public StorePage searchProduct(String text) {
       // searchBox.sendKeys(text);
       // searchBox.sendKeys(Keys.ENTER);
        $(SearchBox).sendKeys(text);
        $(SearchBox).sendKeys(Keys.ENTER);
        return this;
    }

    public StorePage setMinPriceFilter(String text) {
      //  minPriceFilter.sendKeys(text);
        $(minPriceFilter).sendKeys(text);
        return this;
    }

    public StorePage setSneakersType() {
     //   sneakersFilterButton.click();
        $(sneakersFilterButton).click();
        return this;
    }

    public StorePage goSearch() {
       // goButton.click();
        $(goButton).click();
        return this;
    }
}
