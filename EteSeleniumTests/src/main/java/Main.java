import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello selenium!");

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Nemanja\\Desktop\\KTS_Projekat_2020\\ChromeWebDriver\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com");
    }
}
