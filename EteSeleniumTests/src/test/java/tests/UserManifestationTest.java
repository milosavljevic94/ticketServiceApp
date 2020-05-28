package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageClasses.HomePage;
import pageClasses.LoginPage;
import pageClasses.ManifestationListUserPage;
import tests.constants.Constants;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class UserManifestationTest {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    ManifestationListUserPage manifestationListUserPage;

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
    public void testFilterButton(){
        openUserManPage();

        manifestationListUserPage.ensureFilterButtonIsClickable();
        manifestationListUserPage.getFilterButton().click();
        manifestationListUserPage.getFilterButton().click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(manifestationListUserPage.getOneDropDownItem().isDisplayed());
        assertTrue(manifestationListUserPage.getDropdownItemsSize() != 0);
        manifestationListUserPage.getOneDropDownItem().click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //manifestationListUserPage.ensureDropdownItemClickable();
        //manifestationListUserPage.getDropdownItem().
    }


}
