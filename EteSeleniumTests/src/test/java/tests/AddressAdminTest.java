package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pageClasses.*;
import tests.constants.Constants;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AddressAdminTest {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    AdminUserPage adminUserPage;

    AdminAddressPage addressPage;

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
        adminUserPage = PageFactory.initElements(chromeBrowser, AdminUserPage.class);
        addressPage = PageFactory.initElements(chromeBrowser, AdminAddressPage.class);
        alertWindow = PageFactory.initElements(chromeBrowser, AlertWindow.class);
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }

    public void openAdminAddressPage() throws InterruptedException {
        homePage.ensureLoginIsClickable();
        homePage.getLoginButton().click();

        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.USERNAME_ADMIN);
        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.PASS_ADMIN);
        loginPage.getLoginButton().click();
        assertTrue(alertWindow.isAlertPresent());
        alertWindow.acceptAlert();

        adminUserPage.getAddressButton().click();
        Thread.sleep(500);
        assertEquals(Constants.APP_HOME_URL+"admin"+"/address", chromeBrowser.getCurrentUrl());
    }

    @Test(priority = 1)
    public void createNewAddressEmptyFields_thenShowAlert() throws InterruptedException {
        openAdminAddressPage();

        addressPage.ensureCreateAddressBtnIsClickable();
        addressPage.getCreateAddressButton().click();

        Thread.sleep(500);

        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "Please fill in all fields!");
        alertWindow.acceptAlert();
    }

    @Ignore
    @Test(priority = 2)
    public void createNewAddressCharInNumberFields_thenShowAlert() throws InterruptedException {
        openAdminAddressPage();

        addressPage.ensureCreateAddressBtnIsClickable();

        addressPage.getStateInput().sendKeys("TEST_STATE");
        addressPage.getCityInput().sendKeys("TEST_CITY");
        addressPage.getStreetInput().sendKeys("TEST_STREET");
        addressPage.getNumberInput().sendKeys("12B");
        addressPage.getLongitudeInput().click();
        addressPage.getLongitudeInput().sendKeys("INVALID INPUT");
        addressPage.getLatitudeInput().click();
        addressPage.getLatitudeInput().sendKeys("INVALID INPUT");
        addressPage.getCreateAddressButton().click();

        Thread.sleep(500);

        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "Please fill in all fields!");
        alertWindow.acceptAlert();
    }

    @Test(priority = 3)
    public void createNewAddressAndDeleteLastAdded() throws InterruptedException {
        openAdminAddressPage();

        //add new address and check size
        addressPage.ensureCreateAddressTableIsDisplayed();

        int sizeBeforeAdd = addressPage.addressSize();

        addressPage.getStateInput().sendKeys("TEST_STATE");
        addressPage.getCityInput().sendKeys("TEST_CITY");
        addressPage.getStreetInput().sendKeys("TEST_STREET");
        addressPage.getNumberInput().sendKeys("12B");
        addressPage.getLongitudeInput().sendKeys("12.2");
        addressPage.getLatitudeInput().sendKeys("22.2");

        addressPage.ensureCreateAddressBtnIsClickable();
        addressPage.getCreateAddressButton().click();
        Thread.sleep(500);

        int sizeAfterAdd = addressPage.addressSize();

        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);

        //then delete last address and check size
        addressPage.getLastAddressDeleteButton().click();
        Thread.sleep(500);
        int sizeAfterDelete = addressPage.addressSize();
        assertEquals(sizeAfterAdd - 1, sizeAfterDelete);
    }

}
