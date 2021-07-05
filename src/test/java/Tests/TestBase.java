package Tests;

import Pages.StorePage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static com.codeborne.selenide.Configuration.browser;

public class TestBase {


    StorePage storePage;

    @Before
    public void SetUp(){
        storePage = new StorePage();
        Configuration.startMaximized = true;
        browser = "chrome";
        Selenide.open("https://www.amazon.com/");

        // browser ="firefox";
        // browser = "safari";
        Configuration.holdBrowserOpen = true;


    }

    @After
    public void Close(){
       // WebDriverRunner.getWebDriver().quit();
    }
}
