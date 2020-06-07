package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageClasses.*;
import tests.constants.Constants;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class UserManifestationTests {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    ManifestationListUserPage manifestationListUserPage;

    ManifestationPage manifestationPage;

    TicketsListUserPage userTickets;

    ReservationsListUserPage userReservations;

    AlertWindow alertWindow;

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
        manifestationPage = PageFactory.initElements(chromeBrowser, ManifestationPage.class);
        userTickets = PageFactory.initElements(chromeBrowser, TicketsListUserPage.class);
        userReservations = PageFactory.initElements(chromeBrowser, ReservationsListUserPage.class);
        alertWindow = PageFactory.initElements(chromeBrowser, AlertWindow.class);

        js = (JavascriptExecutor) chromeBrowser;
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }


    @Test(priority = 1)
    public void testFilterButton() throws InterruptedException {
        openUserManPage();

        //choose option and filter manifestations
        manifestationListUserPage.ensureFilterButtonIsClickable();
        manifestationListUserPage.getFilterButton().click();

        Thread.sleep(1000);

        assertTrue(manifestationListUserPage.getOneDropDownItem().isDisplayed());
        assertTrue(manifestationListUserPage.getDropdownItemsSize() != 0);
        manifestationListUserPage.getOneDropDownItem().click();

        Thread.sleep(3000);
    }

    @Test(priority = 2)
    public void testNotChooseSectorAndDay_thenShowAlert() throws InterruptedException {
        openUserManPage();

        Thread.sleep(3000);

        //open manifestation
        manifestationListUserPage.ensureClickForMoreButtonsIsClickable();
        manifestationListUserPage.clickOnValidDateManifestation();

        Thread.sleep(3000);

        //click verify
        manifestationPage.ensureVerifyButtonIsClickable();
        manifestationPage.getVerifyBtn().click();


        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You must choose day!");
        alertWindow.acceptAlert();

        Thread.sleep(500);

        //choose just day
        manifestationPage.getFirstDayRadioButton().click();
        manifestationPage.getVerifyBtn().click();

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You must choose sector!");
        alertWindow.acceptAlert();

        Thread.sleep(500);

        //try reserve
        manifestationPage.getReserveBtn().click();

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You must choose sector!");
        alertWindow.acceptAlert();

        Thread.sleep(500);

        //and try buy
        manifestationPage.getBuyingBtn().click();

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You must choose sector!");
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }

    @Test(priority = 3)
    public void testWhenOverstepRowColNumber_thenShowAlert() throws InterruptedException {
        openUserManPage();

        Thread.sleep(2000);

        //open valid manifestation
        manifestationListUserPage.ensureMansIsDisplayed();
        manifestationListUserPage.ensureClickForMoreButtonsIsClickable();
        manifestationListUserPage.clickOnValidDateManifestation();

        Thread.sleep(3000);

        //select day and sector
        manifestationPage.ensureBuyingPanelDisplayed();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        manifestationPage.getFirstDayRadioButton().click();
        manifestationPage.getSelectSector().selectByIndex(0);
        Thread.sleep(3000);

        //input not valid seat and click buy
        manifestationPage.getRowInput().sendKeys("100");
        manifestationPage.getColumnInput().sendKeys("100");

        manifestationPage.ensureBuyButtonIsClickable();
        manifestationPage.getBuyingBtn().click();

        Thread.sleep(1000);

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertTrue(alertWindow.getAlertMessage().contains("Please try again and insert correctly seat position!"));
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }

    @Test(priority = 4)
    public void testBuyWhenNotLoggedIn_thenShowAlert() throws InterruptedException {
        //go to manifestations page
        manifestationListUserPage.ensureMansButtonIsClickable();
        manifestationListUserPage.getManifestationButton().click();
        Thread.sleep(500);
        assertEquals(Constants.APP_HOME_URL+"manifestations", chromeBrowser.getCurrentUrl());

        //open not finished manifestation
        manifestationListUserPage.ensureMansIsDisplayed();
        assertFalse(manifestationListUserPage.getNumberOfMans() == 0);
        manifestationListUserPage.ensureClickForMoreButtonsIsClickable();
        manifestationListUserPage.clickOnValidDateManifestation();

        Thread.sleep(3000);

        //select day and sector
        manifestationPage.ensureBuyingPanelDisplayed();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        manifestationPage.getFirstDayRadioButton().click();
        manifestationPage.getSelectSector().selectByIndex(0);
        Thread.sleep(3000);

        //input seat and click buy
        manifestationPage.getRowInput().sendKeys("2");
        manifestationPage.getColumnInput().sendKeys("6");
        manifestationPage.ensureBuyButtonIsClickable();
        manifestationPage.getBuyingBtn().click();

        Thread.sleep(1000);

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You must log in first!");
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }


    @Test(priority = 5)
    public void testBuyTicketForManifestation_thenBuyTicket() throws InterruptedException {
        openUserManPage();

        Thread.sleep(3000);

        //check the ticket number
        manifestationListUserPage.ensureTicketsButtonIsClickable();
        manifestationListUserPage.getTicketsButton().click();
        assertEquals(Constants.APP_HOME_URL+"tickets", chromeBrowser.getCurrentUrl());
        Thread.sleep(500);
        int beforeBuy = userTickets.getNumberOfTickets();

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

        //select day and sector
        manifestationPage.ensureBuyingPanelDisplayed();
        assertTrue(manifestationPage.getBuyingPanel().isDisplayed());

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        assertFalse(manifestationPage.getRadioBtnsSize() == 0);
        manifestationPage.getFirstDayRadioButton().click();

        manifestationPage.getSelectSector().selectByIndex(0);
        Thread.sleep(3000);

        //click verify and show seats
        manifestationPage.ensureVerifyButtonIsClickable();
        manifestationPage.getVerifyBtn().click();
        chromeBrowser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        manifestationPage.ensureSeatsDisplayed();
        assertFalse(manifestationPage.getSeatsImgSize() == 0);

        //input seat and buy
        manifestationPage.getRowInput().sendKeys("4");
        manifestationPage.getColumnInput().sendKeys("5");

        manifestationPage.ensureBuyButtonIsClickable();
        manifestationPage.getBuyingBtn().click();

        Thread.sleep(1000);

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You have successfully bought ticket!");
        alertWindow.acceptAlert();

        Thread.sleep(500);

        //check the ticket number after buy
        assertEquals(Constants.APP_HOME_URL+"tickets", chromeBrowser.getCurrentUrl());
        int afterBuy = userTickets.getNumberOfTickets();
        assertEquals(beforeBuy+1, afterBuy);
    }

    @Test(priority = 6)
    public void testBuyTicketForTakenSeat_thenShowAlert() throws InterruptedException {
        openUserManPage();

        //open the same manifestation
        manifestationListUserPage.ensureMansIsDisplayed();
        assertFalse(manifestationListUserPage.getNumberOfMans() == 0);
        manifestationListUserPage.ensureClickForMoreButtonsIsClickable();
        manifestationListUserPage.clickOnValidDateManifestation();

        Thread.sleep(3000);

        buyTicket();

        Thread.sleep(1000);

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "Seat in row: 4 , and with number:  5  is not free for this day in this sector.Please try to insert another position, or another day or sector.");
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }

    @Test(priority = 7)
    public void testReserveTicketAndCancelReservation() throws InterruptedException {
        openUserManPage();

        Thread.sleep(3000);

        //check the reservations number
        manifestationListUserPage.ensureReservationsButtonIsClickable();
        manifestationListUserPage.getReservationsButton().click();
        assertEquals(Constants.APP_HOME_URL+"reservations", chromeBrowser.getCurrentUrl());
        Thread.sleep(500);
        int beforeRes = userReservations.getNumberOfReservations();


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

        //select day and sector
        manifestationPage.ensureBuyingPanelDisplayed();
        assertTrue(manifestationPage.getBuyingPanel().isDisplayed());

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        assertFalse(manifestationPage.getRadioBtnsSize() == 0);
        manifestationPage.getRandomDayRadioButton().click();

        manifestationPage.getSelectSector().selectByIndex(1);
        Thread.sleep(3000);

        //click verify and show seats
        manifestationPage.ensureVerifyButtonIsClickable();
        manifestationPage.getVerifyBtn().click();
        chromeBrowser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        manifestationPage.ensureSeatsDisplayed();
        assertFalse(manifestationPage.getSeatsImgSize() == 0);

        //input seat and reserve
        manifestationPage.getRowInput().sendKeys("3");
        manifestationPage.getColumnInput().sendKeys("6");

        manifestationPage.ensureReserveButtonIsClickable();
        manifestationPage.getReserveBtn().click();

        Thread.sleep(1000);

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You have successfully made reservation!");
        alertWindow.acceptAlert();

        Thread.sleep(500);

        //check the reservations number after buy
        assertEquals(Constants.APP_HOME_URL+"reservations", chromeBrowser.getCurrentUrl());
        int afterRes = userReservations.getNumberOfReservations();
        assertEquals(beforeRes+1, afterRes);

        Thread.sleep(500);

        //cancel last added reservation
        userReservations.getLastReservation(afterRes).click();
        userReservations.ensureCancelIsClickable();
        userReservations.getCancelResButton().click();

        Thread.sleep(500);

        //alert about canceling reservation
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "Reservation cancelled!");
        alertWindow.acceptAlert();

        Thread.sleep(500);

        int afterCancel = userReservations.getNumberOfReservations();
        assertEquals(afterRes - 1, afterCancel);

        Thread.sleep(500);
    }

    //common methods for tests
    public void openUserManPage() {
        openUserManPage(homePage, loginPage, alertWindow, chromeBrowser);
    }

    static void openUserManPage(HomePage homePage, LoginPage loginPage, AlertWindow alertWindow, WebDriver chromeBrowser) {
        homePage.ensureLoginIsClickable();
        homePage.getLoginButton().click();

        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.VALID_USERNAME);
        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.VALID_PASS);
        loginPage.getLoginButton().click();
        assertTrue(alertWindow.isAlertPresent());
        alertWindow.acceptAlert();
        assertEquals(Constants.APP_HOME_URL+"manifestations", chromeBrowser.getCurrentUrl());
    }

    public void buyTicket() throws InterruptedException {

        //select day and sector
        manifestationPage.ensureBuyingPanelDisplayed();
        assertTrue(manifestationPage.getBuyingPanel().isDisplayed());

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        assertFalse(manifestationPage.getRadioBtnsSize() == 0);
        manifestationPage.getFirstDayRadioButton().click();

        manifestationPage.getSelectSector().selectByIndex(0);

        Thread.sleep(3000);

        //input seat and click buy
        manifestationPage.getRowInput().sendKeys("4");
        manifestationPage.getColumnInput().sendKeys("5");

        manifestationPage.ensureBuyButtonIsClickable();
        manifestationPage.getBuyingBtn().click();

    }
}