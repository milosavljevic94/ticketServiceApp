package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class ReservationsListUserPage {

    private WebDriver webDriver;

    @FindBy(id = "reservationTableDetail")
    private WebElement detailsTable;

    @FindBy(xpath = "//button[contains(text(),'Buy ticket')]")
    private WebElement buyButton;

    @FindBy(xpath = "//button[contains(text(),'Cancel reservation')]")
    private WebElement cancelResButton;

    public ReservationsListUserPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebElement getBuyButton() {
        return buyButton;
    }

    public WebElement getCancelResButton() {
        return cancelResButton;
    }

    public void ensureBuyIsClickable(){
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(buyButton));
    }

    public void ensureCancelIsClickable(){
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(cancelResButton));
    }

    public void ensureReservationListIsDisplayed() {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resList")));
    }

    public void ensureResDetailsIsDisplayed() {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reservationTableDetail")));
    }

    public void ensureAlertIsPresent() {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public int getNumberOfReservations(){
        return webDriver.findElements(By.xpath("//a[@class = 'nav-link active']")).size();
    }

    public boolean isDetailsTableDisplayed(){
        return detailsTable.isDisplayed();
    }


    public WebElement getRandomReservation() {
        int size = getNumberOfReservations();
        int random = (int) (Math.random() * (size - 1));
        List<WebElement> reservationButtons = new ArrayList<WebElement>();
        reservationButtons.addAll( webDriver.findElements(By.xpath("//a[@class = 'nav-link active']")));

        return reservationButtons.get(random);
    }

    public String getAlertMessage() {
       return webDriver.switchTo().alert().getText();
    }

    public void acceptAlert() {
        webDriver.switchTo().alert().accept();
    }
}
