package tests;

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
import static org.testng.Assert.assertTrue;

public class UsersAndRolesTests {

    private WebDriver chromeBrowser;

    HomePage homePage;

    LoginPage loginPage;

    AdminUserPage adminUserPage;

    AdminRolesPage adminRolesPage;

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
        adminUserPage = PageFactory.initElements(chromeBrowser, AdminUserPage.class);
        adminRolesPage = PageFactory.initElements(chromeBrowser, AdminRolesPage.class);
        registerPage = PageFactory.initElements(chromeBrowser,RegisterPage.class );
        alertWindow = PageFactory.initElements(chromeBrowser, AlertWindow.class);
    }

    @AfterMethod
    public void quitSelenium() {
        chromeBrowser.quit();
    }

    public void openAdminUsersPage() {
        homePage.ensureLoginIsClickable();
        homePage.getLoginButton().click();

        loginPage.getUsernameField().click();
        loginPage.setUsernameValue(Constants.USERNAME_ADMIN);
        loginPage.getPasswordField().click();
        loginPage.setPassValue(Constants.PASS_ADMIN);
        loginPage.getLoginButton().click();
        assertTrue(alertWindow.isAlertPresent());
        alertWindow.acceptAlert();
        assertEquals(Constants.APP_HOME_URL+"admin"+"/users", chromeBrowser.getCurrentUrl());
    }

    public void openRolesPage() throws InterruptedException {
        openAdminUsersPage();
        Thread.sleep(500);
        adminUserPage.getRolesButton().click();
        assertEquals(Constants.APP_HOME_URL+"admin"+"/roles", chromeBrowser.getCurrentUrl());
    }

    public void registerTestUser(){
        chromeBrowser.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        homePage.getRegisterButton().click();
        //insert valid data
        registerPage.getFirstNameInput().sendKeys(Constants.VALID_FIRST_NAME);
        registerPage.getLastNameInput().sendKeys(Constants.VALID_LAST_NAME);
        registerPage.getEmailInput().sendKeys(Constants.VALID_EMAIL);
        registerPage.getUserNameInput().sendKeys(Constants.VALID_USERNAME_REG);
        registerPage.getPassInput().sendKeys(Constants.VALID_PASS_REG);
        registerPage.getMatchPassInput().sendKeys(Constants.VALID_PASS_REG);

        //click on register
        registerPage.getRegButton().click();

        alertWindow.isAlertPresent();
        alertWindow.acceptAlert();
    }

    @Test(priority = 1)
    public void testUsersPreviewAndDeleteLastUser() throws InterruptedException {
        registerTestUser();
        Thread.sleep(500);
        openAdminUsersPage();

        Thread.sleep(500);

        //table with users is visible
        adminUserPage.ensureUsersTableIsDisplayed();
        assertTrue(adminUserPage.usersSize() > 0);

        //delete last user and check size
        int sizeBeforeDelete = adminUserPage.usersSize();
        adminUserPage.getLastUserDeleteButton().click();
        Thread.sleep(500);
        int sizeAfterDelete = adminUserPage.usersSize();
        assertEquals(sizeBeforeDelete-1, sizeAfterDelete);
    }

    @Test(priority = 2)
    public void createNewRoleEmptyName_thenShowAlert() throws InterruptedException {
        openRolesPage();

        adminRolesPage.ensureCreateRoleIsClickable();
        adminRolesPage.getCreateRoleButton().click();

        Thread.sleep(500);

        assertTrue(alertWindow.isAlertPresent());
        assertEquals(alertWindow.getAlertMessage(), "Role name can not be empty!");
        alertWindow.acceptAlert();
    }

    @Test(priority = 3)
    public void createNewRoleAndDeleteRole() throws InterruptedException {
        openRolesPage();

        //add new role and check size
        adminRolesPage.ensureCreateRoleTableIsDisplayed();

        int sizeBeforeAdd = adminRolesPage.rolesSize();

        adminRolesPage.getRoleNameInput().sendKeys("TEST_ROLE");
        adminRolesPage.ensureCreateRoleIsClickable();
        adminRolesPage.getCreateRoleButton().click();
        Thread.sleep(500);

        int sizeAfterAdd = adminRolesPage.rolesSize();

        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);

        //then delete last role and check size
        adminRolesPage.getLastRoleDeleteButton().click();
        Thread.sleep(500);
        int sizeAfterDelete = adminRolesPage.rolesSize();
        assertEquals(sizeAfterAdd - 1, sizeAfterDelete);
    }
}
