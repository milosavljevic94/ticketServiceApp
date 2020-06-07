package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage {

    private WebDriver driver;

    @FindBy(id = "firstNameInput")
    private WebElement firstNameInput;

    @FindBy(id = "lastNameInput")
    private WebElement lastNameInput;

    @FindBy(id = "emailInput")
    private WebElement emailInput;

    @FindBy(id = "usernameInput")
    private WebElement userNameInput;

    @FindBy(id = "passInput")
    private WebElement passInput;

    @FindBy(id = "matchPassInput")
    private WebElement matchPassInput;

    @FindBy(id = "regButtonId")
    private WebElement regButton;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    //ensures
    public void ensureRegisterButtonIsClickable() {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(regButton));
    }

    //getters
    public WebElement getFirstNameInput() {
        return firstNameInput;
    }

    public WebElement getLastNameInput() {
        return lastNameInput;
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getUserNameInput() {
        return userNameInput;
    }

    public WebElement getPassInput() {
        return passInput;
    }

    public WebElement getMatchPassInput() {
        return matchPassInput;
    }

    public WebElement getRegButton() {
        return regButton;
    }

    public boolean requiredErrorIsVisible(){
       return driver.findElements(By.xpath("//div[contains(text(), 'required')]")).size() > 0;
    }

    public boolean passErrorIsVisible(){
        return driver.findElements(By.xpath("//div[contains(text(), 'Password must be at least 6 characters')]")).size() > 0;
    }
}
