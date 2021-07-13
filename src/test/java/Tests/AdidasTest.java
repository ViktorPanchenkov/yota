package Tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import org.junit.Assert;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;


public class AdidasTest extends TestBase {


    @Epic("Testing For https://www.amazon.com/  Search")
    @Feature("Search testing")
    @Severity(SeverityLevel.CRITICAL)
    @Description("In this test we will find sneakers with adidas name and see list of results")
    @Story("Test search with valid data - adidas")
    @Test
    public void SearchAdidasWithValidSearchData(){
        String NameOFProduct = "adidas shoes men";
      storePage.searchProduct(NameOFProduct)
              .setSneakersType()
              .setMinPriceFilter("40")
              .goSearch();
        Assert.assertTrue(storePage.IS_SearchResult_Is_Dispalyed());
        Assert.assertTrue(storePage.IS_AllProductshaveAdidasName());
    }
    @Feature("Search testing")
    @Severity(SeverityLevel.NORMAL)
    @Description("In this test we will find lacoste shoes. Then we should see the list of results  ")
    @Story("Test search with valid data - lacoste")
    @Test
    public void SearchLacosta(){
        String NameOFProduct = "lacoste shoes men";
        storePage.searchProduct(NameOFProduct)
                .setMinPriceFilter("40")
                .goSearch();
        Assert.assertTrue(storePage.IS_SearchResult_Is_Dispalyed());
        Assert.assertTrue(storePage.IS_All_Products_Have_LacosteName());
    }




}
