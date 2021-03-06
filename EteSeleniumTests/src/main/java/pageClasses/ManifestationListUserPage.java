package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import sun.awt.windows.WEmbeddedFrame;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    @FindBy(className = "moreLink")
    private List<WebElement> clickForMoreBtns;

    @FindBy(xpath = "//div[@class = 'card rounded']")
    private List<WebElement> manCards;

    public ManifestationListUserPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //size of items.
    public int getNumberOfMans(){
        return manCards.size();
    }

    public int getDropdownItemsSize(){
        return webDriver.findElements(By.xpath("//a[@class = 'dropdown-item']")).size();
    }

    //ensures
    public void ensureMansIsDisplayed() {
        (new WebDriverWait(webDriver, 20)).until(ExpectedConditions.visibilityOfAllElements(manCards));
    }

    public void ensureTicketsButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(ticketsButton));
    }

    public void ensureMansButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(manifestationButton));
    }

    public void ensureReservationsButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(reservationsButton));
    }

    public void ensureFilterButtonIsClickable(){
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(filterButton));
    }

    public void ensureClickForMoreButtonsIsClickable(){
        for (int i = 0; i< clickForMoreBtns.size(); i++){
            (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(clickForMoreBtns.get(i)));
        }
    }

    //getters
    public WebElement getTicketsButton() {
        return ticketsButton;
    }

    public WebElement getReservationsButton() {
        return reservationsButton;
    }

    public WebElement getFilterButton() {
        return filterButton;
    }

    public WebElement getManifestationButton() {
        return manifestationButton;
    }


    public void clickOnValidDateManifestation(){

        for (int i = 0; i< manCards.size(); i++){
            WebElement card = manCards.get(i);
            WebElement date = card.findElement(By.tagName("b"));
            String dateString = date.getText();
            String[] parts = dateString.split(" ");
            String startDate = parts[0];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateTime = LocalDate.parse(startDate, formatter);

            if (dateTime.isBefore(LocalDate.now())){
                continue;
            }else{
                WebElement clickForMore = card.findElement(By.linkText("Click for more"));
                clickForMore.click();
            }
        }
    }

    public void clickOnPassedDateManifestation(){

        for (int i = 0; i< manCards.size(); i++){
            WebElement card = manCards.get(i);
            WebElement date = card.findElement(By.tagName("b"));
            String dateString = date.getText();
            String[] parts = dateString.split(" ");
            String startDate = parts[0];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateTime = LocalDate.parse(startDate, formatter);

            if (dateTime.isBefore(LocalDate.now())){
                WebElement clickForMore = card.findElement(By.linkText("Click for more"));
                clickForMore.click();
            }else{
                continue;
            }
        }
    }

    public void clickOnReserveLimitManifestation() {

        for (int i = 0; i < manCards.size(); i++) {
            WebElement card = manCards.get(i);
            WebElement date = card.findElement(By.tagName("b"));
            String dateString = date.getText();
            String[] parts = dateString.split(" ");
            String startDate = parts[0];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate start = LocalDate.parse(startDate, formatter);

            long days = ChronoUnit.DAYS.between(LocalDate.now(), start);
            int intDays = (int) days;

            if (intDays < 20) {
                WebElement clickForMore = card.findElement(By.linkText("Click for more"));
                clickForMore.click();
            } else {
                continue;
            }
        }
    }

    public WebElement getOneDropDownItem(){
        List<WebElement> items = new ArrayList<WebElement>();
        items = webDriver.findElements(By.xpath("//a[@class = 'dropdown-item']"));
        return items.get(1);
    }
}
