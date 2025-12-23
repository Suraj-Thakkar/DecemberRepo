package com.package1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

@Test
public void MoneyAdd() throws Exception {
        System.out.println("--- STEP 1: INITIALIZING TEST ---");
        startRecording("MoneyAdd_Flow");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080", "--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
        System.out.println("--- STEP 2: NAVIGATING TO CASHBOOK ---");
        driver.get("https://yogi.web.cashbook.in/login");
        System.out.println("URL Loaded: " + driver.getCurrentUrl());

        System.out.println("--- STEP 3: ENTERING PHONE NUMBER ---");
        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber")));
        phone.sendKeys("+911000112587");
        System.out.println("Phone number entered successfully.");

        Thread.sleep(2000);

        System.out.println("--- STEP 4: CLICKING SUBMIT ---");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();

        System.out.println("--- STEP 5: ENTERING OTP ---");
        WebElement otp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']")));
        otp.sendKeys("123456");
        System.out.println("OTP entered.");

        System.out.println("--- STEP 6: VERIFYING LOGIN ---");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Verify']"))).click();

        Thread.sleep(3000);
        System.out.println("SUCCESS: Login sequence completed.");

        } catch (Exception e) {
        System.err.println("FAILURE: Error occurred at " + driver.getCurrentUrl());
        System.err.println("Error Message: " + e.getMessage());
        takeScreenshot(driver, "MoneyAdd_Failure");
        throw e;
        } finally {
        System.out.println("--- STEP 7: CLEANUP & CLOSING ---");
        driver.quit();
        stopRecording();
        }
        }