package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AdminAddressPage {

    private WebDriver driver;

    @FindBy(xpath = "//body//table[1]")
    private WebElement addressTable;

    @FindBy(xpath = "//body//table[2]")
    private WebElement createAddressTable;

    @FindBy(xpath = "//input[@placeholder='State']")
    private WebElement stateInput;

    @FindBy(xpath = "//input[@placeholder='City']")
    private WebElement cityInput;

    @FindBy(xpath = "//input[@placeholder='Name']")
    private WebElement roleNameInput;

    @FindBy(xpath = "//input[@placeholder='Street']")
    private WebElement streetInput;

    @FindBy(xpath = "//input[@placeholder='Number']")
    private WebElement numberInput;

    @FindBy(xpath = "//input[@placeholder='Longitude']")
    private WebElement longitudeInput;

    @FindBy(xpath = "//input[@placeholder='Latitude']")
    private WebElement latitudeInput;

    @FindBy(xpath = "//a[@id='add_row']")
    private WebElement createAddressButton;

    public AdminAddressPage(WebDriver driver) {
        this.driver = driver;
    }

    public int addressSize(){
        return addressTable.findElements(By.tagName("tr")).size();
    }

    public WebElement getLastAddressDeleteButton(){
            List<WebElement> rows = addressTable.findElements(By.tagName("tr"));
            WebElement lastRow = rows.get(addressSize() - 1);
            List<WebElement> cols = lastRow.findElements(By.tagName("td"));
            int colsInLastRow = cols.size();
            WebElement lastDeleteButton = cols.get(colsInLastRow-1).findElement(By.tagName("button"));
            return lastDeleteButton;
    }

    //ensures
    public void ensureAddressTableIsDisplayed() {
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(addressTable));
    }

    public void ensureCreateAddressTableIsDisplayed() {
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(createAddressTable));
    }

    public void ensureCreateAddressBtnIsClickable() {
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(createAddressButton));
    }
    //getters
    public WebElement getStateInput() {
        return stateInput;
    }

    public WebElement getCityInput() {
        return cityInput;
    }

    public WebElement getRoleNameInput() {
        return roleNameInput;
    }

    public WebElement getStreetInput() {
        return streetInput;
    }

    public WebElement getNumberInput() {
        return numberInput;
    }

    public WebElement getLongitudeInput() {
        return longitudeInput;
    }

    public WebElement getLatitudeInput() {
        return latitudeInput;
    }

    public WebElement getCreateAddressButton() {
        return createAddressButton;
    }
}
