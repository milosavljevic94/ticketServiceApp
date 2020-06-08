package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AdminUserPage {

    private WebDriver driver;

    @FindBy(id = "tab_logic")
    private WebElement usersTable;

    @FindBy(id = "rolesAdmin")
    private WebElement rolesButton;

    public AdminUserPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureUsersTableIsDisplayed() {
        (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOf(usersTable));
    }

    public int usersSize(){
        return usersTable.findElements(By.tagName("tr")).size();
    }

    public WebElement getLastUserDeleteButton(){
        List<WebElement> rows = usersTable.findElements(By.tagName("tr"));
        WebElement lastRow = rows.get(usersSize() - 1);
        WebElement lastDeleteButton = lastRow.findElement(By.tagName("button"));
        return lastDeleteButton;
    }

    public WebElement getRolesButton() {
        return rolesButton;
    }
}
