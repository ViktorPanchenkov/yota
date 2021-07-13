package Pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.bs.A;
import org.apache.hc.core5.util.Asserts;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;





import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
            catch(NoSuchElementException ex){
                result.put(name, "$0.00");
            }
        }
        System.out.println(result.keySet());
        System.out.println(result.values());
        return result;
    }

    public void IS_ListOf_Snikers_Displayed(){
        Map<String, String> result = new HashMap<>();
        for(int i = 0 ; i< allSneakers.size(); i++){
            String productName = $(By.cssSelector(".a-size-base-plus.a-color-base.a-text-normal")).getText();
            try {
                String productPrice = $(By.cssSelector(".a-row.a-size-base.a-color-base")).getText();
                result.put(productName,productPrice);
            } catch (AssertionError assertionError){
                result.put(productName,"$0.00");
            }
        }
        System.out.println(allSneakers.size());
        System.out.println(result.size());
        System.out.println(result.keySet());
        System.out.println(result.values());

    }
    public boolean IS_AllProductshaveAdidasName(){
        List<SelenideElement> ProductNames = $$(By.cssSelector(".a-size-base-plus.a-color-base.a-text-normal"));
        System.out.println(ProductNames.size());
        for (int i =0; i < ProductNames.size(); i++){
            try {
                ProductNames.get(i).shouldHave(Condition.text("adidas"));
               // System.out.println(ProductNames.get(i).getText());
            } catch (AssertionError assertionError){
                System.err.println(ProductNames.get(i).getText());
                String wrongName = ProductNames.get(i).getText();
                Assert.fail("Product " + wrongName+ " dont have addidas name");

                return false;
            }
        }
        return true;

    }
    public boolean IS_All_Products_Have_LacosteName(){
        List<SelenideElement> ProductNames = $$(By.cssSelector(".a-size-base-plus.a-color-base.a-text-normal"));
        System.out.println(ProductNames.size());
        for (int i =0; i < ProductNames.size(); i++){
            try {
                ProductNames.get(i).shouldHave(Condition.text("Lacoste"));
                // System.out.println(ProductNames.get(i).getText());
            } catch (AssertionError assertionError){
                System.err.println(ProductNames.get(i).getText());
                String wrongName = ProductNames.get(i).getText();
                Assert.fail("Product " + wrongName+ " dont have Lacoste name");

                return false;
            }
        }
        return true;
    }

    public boolean IS_SearchResult_Is_Dispalyed(){
        List<SelenideElement> allResults = $$(By.xpath("//*[contains(@data-component-type,'s-search-result')]"));
        if(allResults != null){
            return true;
        } else {
            Assert.fail("There is no any results!");
            return false;
        }
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
