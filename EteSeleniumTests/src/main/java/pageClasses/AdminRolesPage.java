package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AdminRolesPage {

    private WebDriver driver;

    @FindBy(xpath = "//body//table[1]")
    private WebElement rolesTable;

    @FindBy(xpath = "//body//table[2]")
    private WebElement createRoleTable;

    @FindBy(xpath = "//input[@placeholder='Name']")
    private WebElement roleNameInput;

    @FindBy(xpath = "//a[@id='add_row']")
    private WebElement createRoleButton;

    public AdminRolesPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureRolesTableIsDisplayed() {
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(rolesTable));
    }

    public void ensureCreateRoleTableIsDisplayed() {
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(createRoleTable));
    }

    public void ensureCreateRoleIsClickable() {
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(createRoleButton));
    }

    public int rolesSize(){
        return rolesTable.findElements(By.tagName("tr")).size();
    }

    public WebElement getLastRoleDeleteButton(){
        List<WebElement> rows = rolesTable.findElements(By.tagName("tr"));
        WebElement lastRow = rows.get(rolesSize() - 1);
        List<WebElement> cols = lastRow.findElements(By.tagName("td"));
        int colsInLastRow = cols.size();
        WebElement lastDeleteButton = cols.get(colsInLastRow-1).findElement(By.tagName("button"));
        return lastDeleteButton;
    }

    public WebElement getRoleNameInput() {
        return roleNameInput;
    }

    public WebElement getCreateRoleButton() {
        return createRoleButton;
    }
}
