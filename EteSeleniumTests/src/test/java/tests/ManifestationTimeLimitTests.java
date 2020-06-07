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

import static org.testng.Assert.*;

public class ManifestationTimeLimitTests {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    ManifestationListUserPage manifestationListUserPage;

    ManifestationPage manifestationPage;

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
        alertWindow = PageFactory.initElements(chromeBrowser, AlertWindow.class);

        js = (JavascriptExecutor) chromeBrowser;
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }


    @Test
    public void testOpenFinishedManifestation_thenShowFinished() throws InterruptedException {
        openUserManPage();

        Thread.sleep(2000);

        //open passed manifestation
        manifestationListUserPage.ensureMansIsDisplayed();
        manifestationListUserPage.ensureClickForMoreButtonsIsClickable();
        manifestationListUserPage.clickOnPassedDateManifestation();

        Thread.sleep(3000);

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        manifestationPage.ensureBuyingPanelNotDisplayed();
        assertFalse(manifestationPage.buyingPanelIsPresent());
        manifestationPage.ensureFinishedIsDisplayed();
        assertTrue(manifestationPage.getFinishedImg().isDisplayed());

        Thread.sleep(500);
    }

    @Test
    public void testTryReserveLimit_thenShowAlert() throws InterruptedException {
        openUserManPage();

        Thread.sleep(2000);

        //open reserve limited manifestation
        manifestationListUserPage.ensureMansIsDisplayed();
        manifestationListUserPage.ensureClickForMoreButtonsIsClickable();
        manifestationListUserPage.clickOnReserveLimitManifestation();

        Thread.sleep(3000);

        //select day and sector
        manifestationPage.ensureBuyingPanelDisplayed();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        manifestationPage.getRandomDayRadioButton().click();
        manifestationPage.getSelectSector().selectByIndex(1);

        Thread.sleep(2000);

        //input seat and reserve
        manifestationPage.getRowInput().sendKeys("4");
        manifestationPage.getColumnInput().sendKeys("1");

        manifestationPage.ensureReserveButtonIsClickable();
        manifestationPage.getReserveBtn().click();

        Thread.sleep(1000);

        //check alert
        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "You can't reserve ticket because manifestation starts for 20 days.");
        alertWindow.acceptAlert();

        Thread.sleep(500);
    }

    public void openUserManPage() {
        UserManifestationTests.openUserManPage(homePage, loginPage, alertWindow, chromeBrowser);
    }
}
