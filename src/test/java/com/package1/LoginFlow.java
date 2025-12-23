package com.package1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginFlow {

    @Test
    public void MoneyAdd() throws Exception {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        /** * IMPORTANT FOR VIDEO RECORDING:
         * We REMOVE options.addArguments("--headless=new");
         * This allows the browser to render UI on the Xvfb virtual display (:99)
         */
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        // Increased wait to 60s to handle slower CI environments
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            System.out.println("Starting Test: Navigating to Login Page...");
            driver.get("https://yogi.web.cashbook.in/login");

            // Enter Phone
            WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber")));
            phone.sendKeys("+911000112587");

            // Short sleep to allow UI state to update before click
            Thread.sleep(2000);

            // Submit
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
            System.out.println("Phone submitted, waiting for OTP field...");

            // Enter OTP
            WebElement otp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']")));
            otp.sendKeys("123456");

            // Verify
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Verify']"))).click();

            // Add a 3-second pause at the end so the video captures the successful login state
            Thread.sleep(3000);
            System.out.println("Login sequence completed.");

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            takeScreenshot(driver, "MoneyAdd_Failure");
            throw e;
        } finally {
            driver.quit();
        }
    }

    public void takeScreenshot(WebDriver driver, String fileName) {
        try {
            // Ensure screenshots folder exists
            File folder = new File("./screenshots/");
            if (!folder.exists()) folder.mkdirs();

            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileUtils.copyFile(scrFile, new File("./screenshots/" + fileName + "_" + timestamp + ".png"));
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}