package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageClasses.HomePage;
import pageClasses.LoginPage;
import pageClasses.ManifestationListUserPage;
import pageClasses.TicketsListUserPage;
import tests.constants.Constants;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UserManifestationTest {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    ManifestationListUserPage manifestationListUserPage;

    TicketsListUserPage userTickets;

    JavascriptExecutor js;

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
        userTickets = PageFactory.initElements(chromeBrowser, TicketsListUserPage.class);

        js = (JavascriptExecutor) chromeBrowser;
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }


    public void openUserManPage() {
        homePage.ensureLoginIsClickable();
        homePage.getLoginButton().click();

        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.VALID_USERNAME);
        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.VALID_PASS);
        loginPage.getLoginButton().click();
        assertTrue(loginPage.isAlertPresent());
        loginPage.acceptAlertLogin();
        assertEquals(Constants.APP_HOME_URL+"manifestations", chromeBrowser.getCurrentUrl());
    }

    @Test
    public void testFilterButton() throws InterruptedException {
        openUserManPage();

        //choose option and filter manifestations
        manifestationListUserPage.ensureFilterButtonIsClickable();
        manifestationListUserPage.getFilterButton().click();
        manifestationListUserPage.getFilterButton().click();

        Thread.sleep(3000);

        assertTrue(manifestationListUserPage.getOneDropDownItem().isDisplayed());
        assertTrue(manifestationListUserPage.getDropdownItemsSize() != 0);
        manifestationListUserPage.getOneDropDownItem().click();

        Thread.sleep(5000);
    }

    @Test
    public void testBuyTicketForManifestation_thenBuyTicket() throws InterruptedException {
        openUserManPage();

        Thread.sleep(3000);

        //check the ticket number
        manifestationListUserPage.ensureTicketsButtonIsClickable();
        manifestationListUserPage.getTicketsButton().click();
        assertEquals(Constants.APP_HOME_URL+"tickets", chromeBrowser.getCurrentUrl());
        Thread.sleep(500);
        int beforeBuy = userTickets.getNumberOfTickets();
        System.out.println("before buy je : "+ beforeBuy);

        //return to manifestations
        manifestationListUserPage.ensureMansButtonIsClickable();
        manifestationListUserPage.getManifestationButton().click();
        assertEquals(Constants.APP_HOME_URL+"manifestations", chromeBrowser.getCurrentUrl());


        //open not finished manifestation
        manifestationListUserPage.ensureMansIsDisplayed();
        assertFalse(manifestationListUserPage.getNumberOfMans() == 0);
        manifestationListUserPage.ensureClickForMoreButtonsIsClickable();
        manifestationListUserPage.clickOnValidDateManifestation();

        Thread.sleep(3000);

        //input and click buy ticket
        manifestationListUserPage.ensureBuyingPanelDisplayed();
        assertTrue(manifestationListUserPage.getBuyingPanel().isDisplayed());

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        assertFalse(manifestationListUserPage.getRadioBtnsSize() == 0);
        manifestationListUserPage.getRandomDayRadioButton().click();

        manifestationListUserPage.getSelectSector().selectByIndex(1);
        Thread.sleep(3000);

        manifestationListUserPage.getRowInput().sendKeys("2");
        manifestationListUserPage.getColumnInput().sendKeys("2");

        manifestationListUserPage.ensureBuyButtonIsClickable();
        manifestationListUserPage.getBuyingBtn().click();

        Thread.sleep(1000);

        //alert iskace!!!!!!

        //check the ticket number after buy
        assertEquals(Constants.APP_HOME_URL+"tickets", chromeBrowser.getCurrentUrl());
        int afterBuy = userTickets.getNumberOfTickets();
        System.out.println("before buy je : "+ afterBuy);
        assertEquals(beforeBuy+1, afterBuy);
    }

    @Test
    public void testReserveTicketForManifestation_thenReserveTicket(){
        // TODO: 29.5.2020: test steps for preview manifestation and reserve ticket.
    }


}
