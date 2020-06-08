package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageClasses.AlertWindow;
import pageClasses.HomePage;
import pageClasses.LoginPage;
import pageClasses.RegisterPage;
import tests.constants.Constants;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RegistrationTests {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    RegisterPage registerPage;

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
        registerPage = PageFactory.initElements(chromeBrowser, RegisterPage.class);
        alertWindow = PageFactory.initElements(chromeBrowser, AlertWindow.class);
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }

    public void openRegisterPage() {
        homePage.ensureRegisterIsClickable();
        homePage.getRegisterButton().click();

        chromeBrowser.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS );
        //check are register page open successfully.
        assertEquals(Constants.APP_HOME_URL + "register", chromeBrowser.getCurrentUrl());
    }


    @Test(priority = 1)
    public void testRegisterSuccess_thenRegisterAndGoToLogin() throws InterruptedException {
        openRegisterPage();

        Thread.sleep(500);

        //insert valid data
        registerPage.getFirstNameInput().sendKeys(Constants.VALID_FIRST_NAME);
        registerPage.getLastNameInput().sendKeys(Constants.VALID_LAST_NAME);
        registerPage.getEmailInput().sendKeys(Constants.VALID_EMAIL);
        registerPage.getUserNameInput().sendKeys(Constants.VALID_USERNAME_REG);
        registerPage.getPassInput().sendKeys(Constants.VALID_PASS_REG);
        registerPage.getMatchPassInput().sendKeys(Constants.VALID_PASS_REG);

        //click on register
        registerPage.ensureRegisterButtonIsClickable();
        registerPage.getRegButton().click();

        alertWindow.isAlertPresent();
        assertEquals(alertWindow.getAlertMessage(), "Register complete, now login with your credentials!");
        alertWindow.acceptAlert();

        Thread.sleep(500);

        assertEquals(chromeBrowser.getCurrentUrl(), Constants.APP_HOME_URL + "login");

        //try to log in with new credentials
        loginPage.setUsernameValue(Constants.VALID_USERNAME_REG);
        loginPage.setPassValue(Constants.VALID_PASS_REG);
        loginPage.getLoginButton().click();

        assertTrue(alertWindow.isAlertPresent());
        assertEquals("Login successful!", alertWindow.getAlertMessage());
        alertWindow.acceptAlert();

        Thread.sleep(500);

        assertEquals(Constants.APP_HOME_URL + "manifestations", chromeBrowser.getCurrentUrl());
    }

    @Test(priority = 2)
    public void testRegisterWhenEmailExist_thenShowAlert() throws InterruptedException {
        openRegisterPage();

        Thread.sleep(500);

        //insert same data for email
        registerPage.getFirstNameInput().sendKeys("test");
        registerPage.getLastNameInput().sendKeys("test");
        registerPage.getEmailInput().sendKeys(Constants.VALID_EMAIL);
        registerPage.getUserNameInput().sendKeys("test");
        registerPage.getPassInput().sendKeys(Constants.VALID_PASS_REG);
        registerPage.getMatchPassInput().sendKeys(Constants.VALID_PASS_REG);

        //click on register
        registerPage.ensureRegisterButtonIsClickable();
        registerPage.getRegButton().click();

        //show alert with error
        alertWindow.isAlertPresent();
        assertEquals(alertWindow.getAlertMessage(), "Email and/or username already exist!");
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }

    @Test(priority = 3)
    public void testRegisterWhenUsernameExist_thenShowAlert() throws InterruptedException {
        openRegisterPage();

        Thread.sleep(500);

        //insert same data for username
        registerPage.getFirstNameInput().sendKeys("test");
        registerPage.getLastNameInput().sendKeys("test");
        registerPage.getEmailInput().sendKeys("somevalidEmail@gmail.com");
        registerPage.getUserNameInput().sendKeys(Constants.VALID_USERNAME_REG);
        registerPage.getPassInput().sendKeys(Constants.VALID_PASS_REG);
        registerPage.getMatchPassInput().sendKeys(Constants.VALID_PASS_REG);

        //click on register
        registerPage.ensureRegisterButtonIsClickable();
        registerPage.getRegButton().click();

        //show alert with error
        alertWindow.isAlertPresent();
        assertEquals(alertWindow.getAlertMessage(), "Username already exist!");
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }

    @Test(priority = 4)
    public void testAllFieldsRequired_thenShowError() throws InterruptedException {
        openRegisterPage();

        Thread.sleep(500);

        //just click on field and don`t insert data
        registerPage.getFirstNameInput().click();
        registerPage.getLastNameInput().click();
        registerPage.getEmailInput().click();
        registerPage.getUserNameInput().click();
        registerPage.getPassInput().click();
        registerPage.getMatchPassInput().click();

        //click on register
        registerPage.ensureRegisterButtonIsClickable();
        registerPage.getRegButton().click();

        //error is visible
        assertTrue(registerPage.requiredErrorIsVisible());
    }

    @Test(priority = 5)
    public void testRegisterWhenPasswordIsShort_thenShowError() throws InterruptedException {
        openRegisterPage();

        Thread.sleep(500);

        //insert short password
        registerPage.getFirstNameInput().sendKeys("test");
        registerPage.getLastNameInput().sendKeys("test");
        registerPage.getEmailInput().sendKeys("somevalidEmail@gmail.com");
        registerPage.getUserNameInput().sendKeys("test");
        registerPage.getPassInput().sendKeys("12");
        registerPage.getMatchPassInput().sendKeys("12");

        //click on register
        registerPage.ensureRegisterButtonIsClickable();
        registerPage.getRegButton().click();

        //error is visible
        assertTrue(registerPage.passErrorIsVisible());
    }

    @Test(priority = 6)
    public void testPasswordsAreNotMatching_thenShowAlert() throws InterruptedException {
        openRegisterPage();

        Thread.sleep(500);

        //insert same data for username
        registerPage.getFirstNameInput().sendKeys("test");
        registerPage.getLastNameInput().sendKeys("test");
        registerPage.getEmailInput().sendKeys("somevalidEmail@gmail.com");
        registerPage.getUserNameInput().sendKeys("someusername");
        registerPage.getPassInput().sendKeys(Constants.VALID_PASS_REG);
        registerPage.getMatchPassInput().sendKeys("notsamepassword");

        //click on register
        registerPage.ensureRegisterButtonIsClickable();
        registerPage.getRegButton().click();

        //show alert with error
        alertWindow.isAlertPresent();
        assertEquals(alertWindow.getAlertMessage(), "Password and matching password is not same!");
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }
}
