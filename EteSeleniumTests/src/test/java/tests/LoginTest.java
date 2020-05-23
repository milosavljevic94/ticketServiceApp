package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageClasses.HomePage;
import pageClasses.LoginPage;
import tests.constants.Constants;

import static org.testng.Assert.*;

public class LoginTest {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    @BeforeMethod
    public void setup() {

        System.setProperty("webdriver.chrome.driver", Constants.WEB_DRIVER_PATH);

        chromeBrowser = new ChromeDriver();
        chromeBrowser.manage().window().maximize();
        chromeBrowser.navigate().to(Constants.APP_HOME_URL);

        chromeBrowser.manage().deleteAllCookies();

        homePage = PageFactory.initElements(chromeBrowser, HomePage.class);
        loginPage = PageFactory.initElements(chromeBrowser, LoginPage.class);
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }

    private void openLoginPage() {
        homePage.ensureLoginIsClickable();
        homePage.getLoginButton().click();

        //check are login page open successfully.
        assertEquals(Constants.APP_HOME_URL + "login", chromeBrowser.getCurrentUrl());
    }

    @Test
    public void testLoginBothFieldsEmpty_thenErrorMessages() {

        openLoginPage();
        loginPage.getLoginButton().click();
        assertTrue(loginPage.getUsernameRequiredErrMessage().isDisplayed());
        assertTrue(loginPage.getPasswordRequiredErrMessage().isDisplayed());
    }

    @Test
    public void testLoginPasswordEmpty_thenErrorMessage() {

        openLoginPage();
        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.SOME_USERNAME);
        loginPage.getPasswordField().clear();
        loginPage.getLoginButton().click();
        assertTrue(loginPage.getPasswordRequiredErrMessage().isDisplayed());
    }

    @Test
    public void testLoginUsernameEmpty_thenErrorMessage() {

        openLoginPage();
        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.SOME_PASS);
        loginPage.getUsernameField().clear();
        loginPage.getLoginButton().click();
        assertTrue(loginPage.getUsernameRequiredErrMessage().isDisplayed());
    }

    @Test
    public void testLoginBadCredentials_thenErrorAlert() {

        openLoginPage();
        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.SOME_USERNAME);

        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.SOME_PASS);

        assertTrue(loginPage.getLoginButton().isEnabled());
        loginPage.getLoginButton().click();

        assertTrue(loginPage.isAlertPresent());
        assertEquals("Login failed. Try again!", loginPage.getAlertMessage());
    }

    @Test
    public void testLoginValidCredentials_thenLogin() {

        openLoginPage();
        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.VALID_USERNAME);

        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.VALID_PASS);

        assertTrue(loginPage.getLoginButton().isEnabled());
        loginPage.getLoginButton().click();

        assertTrue(loginPage.isAlertPresent());
        assertEquals("Login successful!", loginPage.getAlertMessage());

        loginPage.acceptAlertLogin();

        assertEquals(Constants.APP_HOME_URL + "manifestations", chromeBrowser.getCurrentUrl());
    }
}
