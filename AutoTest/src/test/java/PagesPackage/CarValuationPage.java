package PagesPackage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CarValuationPage {
    private WebDriver driver;

    // Locators for the car valuation page
    public static String regNumberField = "(//input[@placeholder='Enter reg'])[1]";
    public static String valuationResult = "//div[contains(@class, 'HeroVehicle__component')]";
    public static String searchButton = "//button[@data-cy='valueButton']";

    public CarValuationPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to enter car registration and search for valuation
    public void searchCarByReg(String registration) {
        WebElement regField = driver.findElement(By.xpath(regNumberField));
        regField.clear();
        regField.sendKeys(registration);
        driver.findElement(By.xpath(searchButton)).click();
    }

    // Method to get formatted valuation result from the website
    public String getValuationResult(String registration) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(valuationResult)));
        String fullText = result.getText();

        System.out.println("Raw Valuation Text from UI: \n" + fullText); // Debugging output

        // Split text into lines
        String[] lines = fullText.split("\n");

        // Ensure the output has enough lines
        if (lines.length < 2) {
            throw new AssertionError("Unexpected UI result format: " + fullText);
        }

        // Extract Make, Model, and Year
        String makeAndModel = lines[0].trim(); // First line contains Make & Model
        String year = lines[1].trim(); // Second line contains Year

        // Extract make (first word) and model (remaining part)
        String[] makeModelParts = makeAndModel.split(" ", 2);
        if (makeModelParts.length < 2) {
            throw new AssertionError("Invalid Make & Model format: " + makeAndModel);
        }

        String make = makeModelParts[0].toUpperCase(); // Convert Make to uppercase
        String model = makeModelParts[1].trim(); // Model name as is

        // Correctly format registration number
        String formattedReg = formatRegistration(registration);

        // Construct the final formatted output
        String extractedResult = formattedReg + "," + make + "," + model + "," + year;

        System.out.println("Extracted Valuation (formatted for comparison): " + extractedResult);
        return extractedResult;
    }

    // Helper method to format registration correctly (e.g., "SG18 HTN")
    private String formatRegistration(String registration) {
        // Ensure the space is placed correctly
        if (registration.length() == 7) {
            return registration.substring(0, 4) + " " + registration.substring(4);
        }
        return registration; // Return unchanged if already formatted
    }
}
