package com.package1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class LoginFlow {
    @Test
    public void MoneyAdd() throws Exception {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            driver.get("https://yogi.web.cashbook.in/login");

            // Enter Phone
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber"))).sendKeys("+911000112587");
            Thread.sleep(2000);

            // Submit
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();

            // Enter OTP
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']"))).sendKeys("123456");

            // Verify
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Verify']"))).click();

        } catch (Exception e) {
            // --- Capture Screenshot on Failure ---
            takeScreenshot(driver, "MoneyAdd_Failure");
            System.err.println("Test failed. Screenshot saved to ./screenshots/ folder.");
            throw e;
        } finally {
            driver.quit();
        }
    }

    // Helper Method for Screenshots
    public void takeScreenshot(WebDriver driver, String fileName) {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            FileUtils.copyFile(scrFile, new File("./screenshots/" + fileName + "_" + timestamp + ".png"));
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}

