package pageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class TicketsListUserPage {

    private WebDriver webDriver;

    @FindBy(xpath = "//table[@class='table table-bordered']")
    private WebElement detailsTable;

    public TicketsListUserPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void ensureTicketListIsDisplayed(){
            (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("ul")));
    }

    public void ensureTicketDetailsIsDisplayed(){
        (new WebDriverWait(webDriver, 10)).until(ExpectedConditions.visibilityOf(detailsTable));
    }

    public int getNumberOfTickets(){
        return webDriver.findElements(By.xpath("//a[@class = 'nav-link active']")).size();
    }

    public WebElement getRandomTicket(){

        int size = getNumberOfTickets();
        int random = (int) (Math.random() * (size - 1));
        List<WebElement> ticketsButtons = new ArrayList<WebElement>();
        ticketsButtons.addAll( webDriver.findElements(By.xpath("//a[@class = 'nav-link active']")));

        return ticketsButtons.get(random);
    }

    public WebElement getDetailsTable() {
        return detailsTable;
    }
}
