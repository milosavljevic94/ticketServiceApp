package pageClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver webDriver;

    @FindBy(id = "login")
    private WebElement loginButton;

    @FindBy(id = "register")
    private WebElement registerButton;

    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void ensureLoginIsClickable(){
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(WebElement loginButton) {
        this.loginButton = loginButton;
    }

    public WebElement getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(WebElement registerButton) {
        this.registerButton = registerButton;
    }
}
