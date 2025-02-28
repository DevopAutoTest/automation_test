package PagesPackage;

import org.openqa.selenium.WebDriver;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutomationPage {
    private WebDriver driver;

    // Constants for the file paths
    public static final String CAR_INPUT_FILE = "/Users/haseeburrahman/Desktop/AutomationTest/src/test/java/PagesPackage/car_input.txt";
    public static final String CAR_OUTPUT_FILE = "/Users/haseeburrahman/Desktop/AutomationTest/src/test/java/PagesPackage/car_output.txt";

    public AutomationPage(WebDriver driver) {
        this.driver = driver;
    }

    // Helper method to read the file content
    public String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    public void goToTheLoginPage() {
        // Define the expected URL
        driver.get("https://motorway.co.uk/");
    }

    public void performTest() {
        try {
            // Read input and output files
            String inputFileContent = readFile(CAR_INPUT_FILE);
            String outputFileContent = readFile(CAR_OUTPUT_FILE);

            // Extract car registration numbers using regex
            Pattern pattern = Pattern.compile("[A-Z]{2}[0-9]{2} [A-Z]{3}");
            Matcher matcher = pattern.matcher(inputFileContent);

            // Create CarValuationPage object
            CarValuationPage carValuationPage = new CarValuationPage(driver);

            while (matcher.find()) {
                String registration = matcher.group();
                System.out.println("Searching valuation for car registration: " + registration);

                // Perform car valuation search
                carValuationPage.searchCarByReg(registration);

                // Wait for results to load and retrieve valuation result
                String valuationResult = carValuationPage.getValuationResult(registration);
                System.out.println("Valuation result: " + valuationResult);

                // Check if valuation matches the expected value from the output file
                if (!outputFileContent.contains(valuationResult)) {
                    throw new AssertionError("Valuation for registration " + registration + " does not match the expected result.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
