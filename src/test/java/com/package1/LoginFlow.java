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
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://yogi.web.cashbook.in/login");
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber"))).sendKeys("+911000112587");
        Thread.sleep(6000);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']"))).sendKeys("123456");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Verify']"))).click();
    }
}

