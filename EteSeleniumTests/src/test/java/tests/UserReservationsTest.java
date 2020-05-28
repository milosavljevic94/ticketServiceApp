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

public class UserReservationsTest {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    ManifestationListUserPage manifestationListUserPage;

    ReservationsListUserPage reservationsListUserPage;

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
        reservationsListUserPage = PageFactory.initElements(chromeBrowser, ReservationsListUserPage.class);
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }

    public void openUserReservationsPage() {
        homePage.ensureLoginIsClickable();
        homePage.getLoginButton().click();

        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.VALID_USERNAME);
        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.VALID_PASS);
        loginPage.getLoginButton().click();
        assertTrue(loginPage.isAlertPresent());
        loginPage.acceptAlertLogin();
        manifestationListUserPage.ensureReservationsButtonIsClickable();
        manifestationListUserPage.getReservationsButton().click();
    }

    @Test(priority = 1)
    public void testClickOnResAndBuy_thenDisplayAndBuy(){
        openUserReservationsPage();

        assertEquals(Constants.APP_HOME_URL + "reservations", chromeBrowser.getCurrentUrl());

        reservationsListUserPage.ensureReservationListIsDisplayed();
        assertTrue(reservationsListUserPage.getNumberOfReservations() != 0);

        //click on some random reservation and display details.
        WebElement randomRes = reservationsListUserPage.getRandomReservation();

        assertTrue(randomRes.isEnabled());
        randomRes.click();

        reservationsListUserPage.ensureResDetailsIsDisplayed();
        assertTrue(reservationsListUserPage.isDetailsTableDisplayed());

        int sizeBefore = reservationsListUserPage.getNumberOfReservations();

        //click to buy ticket.
        reservationsListUserPage.ensureBuyIsClickable();
        reservationsListUserPage.getBuyButton().click();
        reservationsListUserPage.ensureAlertIsPresent();
        assertEquals("Ticket bought successfully!", reservationsListUserPage.getAlertMessage() );
        reservationsListUserPage.acceptAlert();

        chromeBrowser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        int sizeAfter = reservationsListUserPage.getNumberOfReservations();

        assertEquals( sizeAfter,sizeBefore - 1);
    }

    @Test(priority = 2)
    public void testClickOnResAndCancel_thenDisplayAndDeleteReservation(){
        openUserReservationsPage();

        assertEquals(Constants.APP_HOME_URL + "reservations", chromeBrowser.getCurrentUrl());

        reservationsListUserPage.ensureReservationListIsDisplayed();
        assertTrue(reservationsListUserPage.getNumberOfReservations() != 0);

        //click on some random reservation and display details.
        WebElement randomRes = reservationsListUserPage.getRandomReservation();

        assertTrue(randomRes.isEnabled());
        randomRes.click();

        reservationsListUserPage.ensureResDetailsIsDisplayed();
        assertTrue(reservationsListUserPage.isDetailsTableDisplayed());

        int sizeBefore = reservationsListUserPage.getNumberOfReservations();

        //click to buy ticket.
        reservationsListUserPage.ensureCancelIsClickable();
        reservationsListUserPage.getCancelResButton().click();
        reservationsListUserPage.ensureAlertIsPresent();
        assertEquals("Reservation cancelled!", reservationsListUserPage.getAlertMessage() );
        reservationsListUserPage.acceptAlert();

        chromeBrowser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        int sizeAfter = reservationsListUserPage.getNumberOfReservations();

        chromeBrowser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        assertEquals(sizeAfter,sizeBefore - 1);
    }
}
