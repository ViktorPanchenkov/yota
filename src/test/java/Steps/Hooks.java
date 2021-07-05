package Steps;

import Pages.StorePage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.en.Given;
import org.junit.Before;

import static com.codeborne.selenide.Configuration.browser;

public class Hooks {
    StorePage storePage = new StorePage();
    @Before
    @Given("I Open browzer and navigate to Store Page")
    public void OpenPage(){
        storePage = new StorePage();
        Configuration.startMaximized = true;
        browser = "chrome";
        Selenide.open("https://www.amazon.com/");
}
}
