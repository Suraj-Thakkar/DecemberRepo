package com.package1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginFlow {
    @Test
    public void MoneyAdd() throws Exception {
        WebDriverManager.chromedriver().setup();

        // --- Headless Configuration Start ---
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Runs Chrome without a UI
        options.addArguments("--no-sandbox"); // Fixes issues in Linux environments
        options.addArguments("--disable-dev-shm-usage"); // Prevents memory issues in containers
        options.addArguments("--window-size=1920,1080"); // Sets a consistent screen resolution
        // --- Headless Configuration End ---

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://yogi.web.cashbook.in/login");
            // driver.manage().window().maximize(); // Not strictly needed in headless, but harmless

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber"))).sendKeys("+911000112587");

            // Note: Thread.sleep is generally discouraged, but kept here as per your original logic
            Thread.sleep(6000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']"))).sendKeys("123456");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Verify']"))).click();

            System.out.println("Login attempt completed successfully.");
        } finally {
            // It is important to quit the driver to free up resources
            driver.quit();
        }
    }
}

