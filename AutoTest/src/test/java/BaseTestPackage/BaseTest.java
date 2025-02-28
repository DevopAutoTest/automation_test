package BaseTestPackage;

import PagesPackage.AutomationPage;
import PagesPackage.CarValuationPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

public class BaseTest {
    public WebDriver driver;
    public AutomationPage automationPage;
    public CarValuationPage carValuationPage;

    @BeforeMethod
    public void setUpTest() throws InterruptedException {
        // Initialize the WebDriver
        driver = new ChromeDriver();

        // Clear all Cookies
        driver.manage().deleteAllCookies();
        // Maximize the browser window
        driver.manage().window().maximize();

        // Initialize page objects
        automationPage = new AutomationPage(driver);
        carValuationPage = new CarValuationPage(driver);

        // Navigate to the login page or other necessary actions
        automationPage.goToTheLoginPage();
    }

//    @AfterMethod
//    public void tearDown() {
//        // Quit the browser after the test
//        if (driver != null) {
//            driver.quit();
//        }
//    }
}
