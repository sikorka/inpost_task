package eu.inpost.gui;

import eu.inpost.gui.pages.FindParcelPage;
import eu.inpost.gui.pages.HomePage;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import lombok.extern.log4j.Log4j2;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

import static eu.inpost.util.Translations.*;

@Log4j2
public class SearchSteps {

    private WebDriver driver;

    HomePage homePage;
    FindParcelPage findParcelPage;


    @Given("InPost website is open")
    public void inpostWebsiteIsOpen() {
        homePage.get();
        homePage.closeCookiesPopup();
    }

    @When("I search for package {string}")
    public void iSearchForPackage(String packageNumber) {
        findParcelPage = homePage.inputPackageNumberAndSubmit(packageNumber);
    }

    @Then("status should be {string}")
    public void statusShouldBe(String packageStatus) {
        findParcelPage.get();

        String statusTranslated = packageStatus;

        switch (packageStatus) {
            case "Collected": statusTranslated = getTranslations().getStatuses().getCollected(); break;
            case "Label nullified" : statusTranslated = getTranslations().getStatuses().getLabel_nullified(); break;
            case "Passed for delivery" : break; //don't know the translation
        }

        findParcelPage.isOfStatus(statusTranslated);
    }

    @Before()
    public void openBrowser() throws MalformedURLException {
        driver = WebDriverFactory.createWebDriver();
        
        homePage = new HomePage(driver);
        findParcelPage = new FindParcelPage(driver);
    }

    @After()
    public void closeBrowser() {
        driver.quit();
    }

    @AfterStep()
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot");
        }
    }
}
