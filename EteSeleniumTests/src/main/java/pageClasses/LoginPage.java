package pageClasses;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;

    @FindBy(id = "username-login-input")
    private WebElement usernameField;

    @FindBy(id = "password-login-input")
    private WebElement passwordField;

    @FindBy(id = "login-btn")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(text(),'Username is required')]")
    private WebElement usernameRequiredErrMessage;

    @FindBy(xpath = "//div[contains(text(),'Password is required')]")
    private WebElement passwordRequiredErrMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getUsernameField() {
        return usernameField;
    }

    public void setUsernameValue(String username){
        this.usernameField.clear();
        this.usernameField.sendKeys(username);
    }

    public void setPassValue(String p){
        this.passwordField.clear();
        this.passwordField.sendKeys(p);
    }

    public boolean isAlertPresent() {
        boolean foundAlert = false;
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            foundAlert = true;
        } catch (TimeoutException eTO) {
            foundAlert = false;
        }
        return foundAlert;
    }

    public void acceptAlertLogin(){
        driver.switchTo().alert().accept();
    }

    public String getAlertMessage(){
        return driver.switchTo().alert().getText();
    }

    public WebElement getPasswordField() {
        return passwordField;
    }

    public WebElement getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(WebElement loginButton) {
        this.loginButton = loginButton;
    }

    public WebElement getUsernameRequiredErrMessage() {
        return usernameRequiredErrMessage;
    }

    public WebElement getPasswordRequiredErrMessage() {
        return passwordRequiredErrMessage;
    }
}
