package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class ManifestationListUserPage {

    private WebDriver webDriver;

    @FindBy(id = "manUserButton")
    private WebElement manifestationButton;

    @FindBy(id = "ticketsUser")
    private WebElement ticketsButton;

    @FindBy(id = "reservationsUser")
    private WebElement reservationsButton;

    @FindBy(id = "dropdownMenuButton")
    private WebElement filterButton;

    public ManifestationListUserPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //size of manifestation items.
    public int getNumberOfMans(){
        return webDriver.findElements(By.className("card rounded")).size();
    }

    public void ensureTicketsButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(ticketsButton));
    }

    public void ensureReservationsButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(reservationsButton));
    }

    public void ensureFilterButtonIsClickable(){
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(filterButton));
    }

    public int getDropdownItemsSize(){
        return webDriver.findElements(By.xpath("//a[@class = 'dropdown-item']")).size();
    }

    public WebElement getOneDropDownItem(){
        List<WebElement> items = new ArrayList<WebElement>();
        items = webDriver.findElements(By.xpath("//a[@class = 'dropdown-item']"));
        return items.get(1);
    }

    public WebElement getManifestationButton() {
        return manifestationButton;
    }

    public WebElement getTicketsButton() {
        return ticketsButton;
    }

    public WebElement getReservationsButton() {
        return reservationsButton;
    }

    public WebElement getFilterButton() {
        return filterButton;
    }
}
