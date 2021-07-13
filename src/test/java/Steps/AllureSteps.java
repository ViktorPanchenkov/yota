package Steps;

import Pages.StorePage;
import io.qameta.allure.Step;

public class AllureSteps {

    StorePage storePage = new StorePage();

    @Step
    public void SearchProduct(){
        String nameOfProduct = "adidas shoes men";
        storePage.searchProduct(nameOfProduct);
    }
}
