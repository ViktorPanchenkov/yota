package Tests;

import com.codeborne.selenide.Selenide;
import org.junit.Assert;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;


public class AdidasTest extends TestBase {


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
