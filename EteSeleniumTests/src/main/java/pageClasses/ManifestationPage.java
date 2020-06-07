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

public class ManifestationPage {

    private WebDriver webDriver;

    @FindBy(xpath = "//div[@id='buyingPanel']")
    private WebElement buyingPanel;

    @FindBy(name = "row")
    private WebElement rowInput;

    @FindBy(xpath = "//button[contains(text(),'Verify seat')]")
    private WebElement verifyBtn;

    @FindBy(id = "seatPanel")
    private WebElement seatsPanel;

    @FindBy(name = "column")
    private WebElement columnInput;

    @FindBy(xpath = "//button[contains(text(),'Buy ticket')]")
    private WebElement buyingBtn;

    @FindBy(xpath = "//button[contains(text(),'Reserve ticket')]")
    private WebElement reserveBtn;

    @FindBy(id = "finishedAlert")
    private WebElement finishedImg;

    public ManifestationPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    //size
    public int getSeatsImgSize(){
        return webDriver.findElements(By.xpath("//div[@class='mySeat']")).size();
    }

    public int getRadioBtnsSize(){
        return buyingPanel.findElements(By.id("exampleRadios1")).size();
    }

    //ensures
    public void ensureBuyingPanelDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(buyingPanel));
    }

    public void ensureBuyingPanelNotDisplayed() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("buyingPanel")));
    }

    public void ensureBuyButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(buyingPanel.findElement(By.xpath("//button[contains(text(),'Buy ticket')]"))));
    }

    public void ensureReserveButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(reserveBtn));
    }


    public void ensureVerifyButtonIsClickable() {
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.elementToBeClickable(verifyBtn));
    }

    public void ensureSeatsDisplayed(){
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(seatsPanel));
    }

    public void ensureFinishedIsDisplayed(){
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(finishedImg));
    }

    //getters
    public WebElement getBuyingPanel() {
        return buyingPanel;
    }

    public WebElement getRowInput() {
        return rowInput;
    }

    public WebElement getColumnInput() {
        return columnInput;
    }

    public WebElement getBuyingBtn() {
        return buyingBtn;
    }

    public WebElement getVerifyBtn() {
        return verifyBtn;
    }

    public WebElement getReserveBtn() {
        return reserveBtn;
    }

    public WebElement getFinishedImg() {
        return finishedImg;
    }

    public WebElement getRandomDayRadioButton(){

        int size = getRadioBtnsSize();
        int random = (int) (Math.random() * (size - 1));
        List<WebElement> radioButtons = new ArrayList<WebElement>();
        radioButtons.addAll( webDriver.findElements(By.id("exampleRadios1")));

        return radioButtons.get(random);
    }

    public WebElement getFirstDayRadioButton(){

        List<WebElement> radioButtons = new ArrayList<WebElement>();
        radioButtons.addAll( webDriver.findElements(By.id("exampleRadios1")));

        return radioButtons.get(0);
    }

    public boolean buyingPanelIsPresent(){
        return webDriver.findElements(By.id("buyingPanel")).size() > 0;
    }

    public Select getSelectSector() {
        return new Select(webDriver.findElement(By.id("inlineFormCustomSelectPref")));
    }
}
