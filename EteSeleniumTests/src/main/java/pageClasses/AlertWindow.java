package pageClasses;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AlertWindow {

    private WebDriver driver;

    public AlertWindow(WebDriver webDriver) {
        this.driver = webDriver;
    }

    public boolean isAlertPresent() {
        boolean foundAlert = false;
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            foundAlert = true;
        } catch (TimeoutException eTO) {
            foundAlert = false;
        }
        return foundAlert;
    }

    public void acceptAlert(){
        driver.switchTo().alert().accept();
    }

    public String getAlertMessage(){
        return driver.switchTo().alert().getText();
    }
}
