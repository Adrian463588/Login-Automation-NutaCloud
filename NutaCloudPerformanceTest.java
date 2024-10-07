package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class NutaCloudPerformanceTest {

    private WebDriver driver;

    @BeforeClass
    public void setup() {
        // Setup WebDriver
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeMethod
    public void initDriver() {
        // Initialize WebDriver for each test method
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    @Test(invocationCount = 5, threadPoolSize = 1)
    @Parameters({"companyName", "username", "password"})
    public void loginPerformanceTest(
            @Optional("TestQA0710") String companyName,
            @Optional("Adrian Syah Abidin") String username,
            @Optional("#Adrian0710") String password) throws InterruptedException {

        // Start time measurement
        Instant start = Instant.now();

        // Navigate to the login page
        driver.get("https://www.nutacloud.com/authentication/loginv2");

        // Use WebDriverWait for each element to ensure readiness
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Define the XPath for each element
        String companyNameXPath = "/html/body/div/div/form/div[1]/input";
        String usernameXPath = "/html/body/div/div/form/div[2]/input";
        String passwordXPath = "/html/body/div/div/form/div[3]/input";
        String loginButtonXPath = "/html/body/div/div/form/button";

        try {
            // Wait for the company name input to be clickable, then clear and enter data
            WebElement companyNameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(companyNameXPath)));
            companyNameField.clear();
            companyNameField.sendKeys(companyName);

            // Add a 1-second wait after entering the company name
            Thread.sleep(1000);

            // Wait for the username input to be clickable, then clear and enter data
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(usernameXPath)));
            usernameField.clear();
            usernameField.sendKeys(username);

            // Add a 1-second wait after entering the username
            Thread.sleep(1000);

            // Wait for the password input to be clickable, then clear and enter data
            WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(passwordXPath)));
            passwordField.clear();
            passwordField.sendKeys(password);

            // Add a 1-second wait after entering the password
            Thread.sleep(1000);

            // Wait for the login button to be clickable, then click it
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(loginButtonXPath)));
            loginButton.click();

            // Wait for the dashboard to load or timeout after 10 seconds
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Verify successful login by checking if the dashboard element is present
            boolean isLoggedIn = driver.findElements(By.xpath("/html/body/div[1]/div[1]/div/ul/li[2]/a")).size() > 0;

            // End time measurement
            Instant end = Instant.now();
            long timeElapsed = Duration.between(start, end).toMillis();

            // Assert that login was successful and log the time taken
            Assert.assertTrue(isLoggedIn, "Login failed or dashboard element not found!");
            System.out.println("Login successful for user " + username + ". Time taken: " + timeElapsed + " ms");

            // Add a 2-second delay between iterations
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        } finally {
            // Close the browser after each test iteration, whether successful or not
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @AfterClass
    public void tearDown() {
        // Additional cleanup after all tests, if necessary
    }
}
