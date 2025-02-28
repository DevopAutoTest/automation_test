package TestPackage;

import PagesPackage.AutomationPage;
import PagesPackage.CarValuationPage;
import BaseTestPackage.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutomationTest extends BaseTest {

    @DataProvider(name = "carRegistrations")
    public Object[][] getCarRegistrations() {
        List<String> registrations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(AutomationPage.CAR_INPUT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {  // Ensure empty lines are ignored
                    registrations.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert list to Object[][] for DataProvider
        Object[][] data = new Object[registrations.size()][1];
        for (int i = 0; i < registrations.size(); i++) {
            data[i][0] = registrations.get(i);
        }
        return data;
    }

    @Test(priority = 1, dataProvider = "carRegistrations")
    public void testCarValuation(String carReg) {
        try {
            System.out.println("Testing car registration: " + carReg);

            // Search for car valuation
            carValuationPage.searchCarByReg(carReg);

            // Get the valuation result
            String valuationResult = carValuationPage.getValuationResult(carReg);
            System.out.println("Valuation result: " + valuationResult);

            // Read expected output file and check if the valuation matches
            String outputFileContent = automationPage.readFile(AutomationPage.CAR_OUTPUT_FILE);
            if (!outputFileContent.contains(valuationResult)) {
                throw new AssertionError("Valuation for registration " + carReg + " does not match the expected result.");
            }

            System.out.println("Test Passed for: " + carReg);
        } catch (AssertionError e) {
            System.out.println("Test failed for " + carReg + ": " + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
