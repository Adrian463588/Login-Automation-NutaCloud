package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Setup WebDriver using WebDriverManager
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();

        try {
            // Navigate to the login page
            driver.get("https://www.nutacloud.com/authentication/loginv2");

            // Maximize the browser window
            driver.manage().window().maximize();

            // Define the XPath for each element
            String companyNameXPath = "/html/body/div/div/form/div[1]/input";
            String usernameXPath = "/html/body/div/div/form/div[2]/input";
            String passwordXPath = "/html/body/div/div/form/div[3]/input";
            String loginButtonXPath = "/html/body/div/div/form/button";

            // Find elements by XPath and fill in the details
            WebElement companyNameField = driver.findElement(By.xpath(companyNameXPath));
            companyNameField.sendKeys("TestQA0710");

            WebElement usernameField = driver.findElement(By.xpath(usernameXPath));
            usernameField.sendKeys("Adrian Syah Abidin");

            WebElement passwordField = driver.findElement(By.xpath(passwordXPath));
            passwordField.sendKeys("#Adrian0710");

            // Click the Log In button
            WebElement loginButton = driver.findElement(By.xpath(loginButtonXPath));
            loginButton.click();

            // Wait for the URL to change to the dashboard page within 10 seconds
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            boolean loggedIn = waitForUrlToChange(driver, "https://www.nutacloud.com/cloud/main", 10);

            if (loggedIn) {
                // Verify the dashboard element is present after login
                if (isElementPresent(driver, By.xpath("/html/body/div[1]/div[1]/div/ul/li[2]/a"))) {
                    System.out.println("Login successful: Dashboard item detected.");
                } else {
                    System.out.println("Login failed: Dashboard item not found.");
                }

                // Wait for 10 seconds to observe the page before quitting
                TimeUnit.SECONDS.sleep(10);
            } else {
                System.out.println("Login failed: URL did not change to the expected dashboard.");
            }

        } catch (Exception e) {
            // Catch any general exceptions and print the stack trace
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    // Helper function to check if an element exists
    public static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Function to wait for the URL to change to a specific URL within a timeout
    public static boolean waitForUrlToChange(WebDriver driver, String expectedUrl, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        try {
            return wait.until(ExpectedConditions.urlToBe(expectedUrl));
        } catch (TimeoutException e) {
            return false;
        }
    }
}
