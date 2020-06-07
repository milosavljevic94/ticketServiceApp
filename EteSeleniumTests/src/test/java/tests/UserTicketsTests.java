package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageClasses.*;
import tests.constants.Constants;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class UserTicketsTests {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    ManifestationListUserPage manifestationListUserPage;

    TicketsListUserPage ticketsListUserPage;

    AlertWindow alertWindow;

    @BeforeMethod
    public void setup() {

        System.setProperty("webdriver.chrome.driver", Constants.WEB_DRIVER_PATH);

        chromeBrowser = new ChromeDriver();
        chromeBrowser.manage().window().maximize();
        chromeBrowser.navigate().to(Constants.APP_HOME_URL);

        chromeBrowser.manage().deleteAllCookies();

        homePage = PageFactory.initElements(chromeBrowser, HomePage.class);
        loginPage = PageFactory.initElements(chromeBrowser, LoginPage.class);
        manifestationListUserPage = PageFactory.initElements(chromeBrowser, ManifestationListUserPage.class);
        ticketsListUserPage = PageFactory.initElements(chromeBrowser, TicketsListUserPage.class);
        alertWindow = PageFactory.initElements(chromeBrowser, AlertWindow.class);
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }

    public void openUserTicketsPage() {
        homePage.ensureLoginIsClickable();
        homePage.getLoginButton().click();

        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.VALID_USERNAME);
        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.VALID_PASS);
        loginPage.getLoginButton().click();
        assertTrue(alertWindow.isAlertPresent());
        alertWindow.acceptAlert();
        manifestationListUserPage.ensureTicketsButtonIsClickable();
        manifestationListUserPage.getTicketsButton().click();
    }

    @Test(priority = 1)
    public void testNavigateToValidPage_thenDisplayTicketList() throws InterruptedException {
        openUserTicketsPage();

        assertEquals(Constants.APP_HOME_URL + "tickets", chromeBrowser.getCurrentUrl());

        ticketsListUserPage.ensureTicketListIsDisplayed();
        Thread.sleep(500);
        assertTrue(ticketsListUserPage.getNumberOfTickets() != 0);
    }

    @Test(priority = 2)
    public void testIsTicketDetailsDisplayed_thenDisplayDetailTable(){
        openUserTicketsPage();

        ticketsListUserPage.ensureTicketListIsDisplayed();
        chromeBrowser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //click on some random ticket and display details.
        WebElement randomTicket = ticketsListUserPage.getRandomTicket();

        assertTrue(randomTicket.isEnabled());
        randomTicket.click();

        ticketsListUserPage.ensureTicketDetailsIsDisplayed();
        assertTrue(ticketsListUserPage.getDetailsTable().isDisplayed());
    }
}
