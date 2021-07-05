package Steps;

import Pages.StorePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StoreSteps {


    StorePage storePage = new StorePage();

    @When("I Enter text in Search Filed")
    public void EnterText(){
        String text = "adidas shoes men";
        storePage.searchProduct(text);
    }
    @When("I Set Sneakers type")
    public void SetSnikersType(){
        storePage.setSneakersType();
    }
    @And("I Set MinPrice Filter")
    public void SetPriceFilter(){
     storePage.setMinPriceFilter("40");
    }
    @And("I Click on the search button")
    public void ClickOnTheSearchButton(){
        storePage.goSearch();
    }
    @Then("I should See List of Sneakers")
    public void GetAllProducts(){
        storePage.getAllDistinctProducts();
    }
}
