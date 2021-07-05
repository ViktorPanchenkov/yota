package Tests;

import com.codeborne.selenide.Selenide;
import org.junit.Test;


public class AdidasTest extends TestBase {


    @Test
    public void test1(){
        String NameOFProduct = "adidas shoes men";
      storePage.searchProduct("adidas shoes men")
              .setSneakersType()
              .setMinPriceFilter("40")
              .goSearch();
             // getAllDistinctProducts();
    }
}
