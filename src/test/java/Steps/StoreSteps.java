package Steps;

import Pages.StorePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class StoreSteps {


    StorePage storePage = new StorePage();

    @When("I Enter {string} in Search Filed")
    public void EnterText(String text){

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
    @Then("I Should See Search result")
    public void SearchResultDisplayed(){
        Assert.assertTrue(storePage.IS_SearchResult_Is_Dispalyed());
    }
}
