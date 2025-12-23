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

public class LoginFlow {

    private ScreenRecorder screenRecorder;

    @Test
    public void MoneyAdd() throws Exception {
        // Start Recording with a specific name
        startRecording("MoneyAdd_Flow");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            System.out.println("Starting Test: Navigating to Login Page...");
            driver.get("https://yogi.web.cashbook.in/login");

            WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber")));
            phone.sendKeys("+911000112587");

            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']"))).click();

            WebElement otp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']")));
            otp.sendKeys("123456");

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Verify']"))).click();

            Thread.sleep(3000);
            System.out.println("Login sequence completed.");

        } catch (Exception e) {
            takeScreenshot(driver, "MoneyAdd_Failure");
            throw e;
        } finally {
            driver.quit();
            stopRecording();
        }
    }

    // --- VIDEO RECORDING METHODS ---

    public void startRecording(String fileName) throws Exception {
        File file = new File("./recordings/");
        if (!file.exists()) file.mkdirs();

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        // FIX: Define the capture area (Rectangle)
        Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

  // Inside startRecording or SpecializedScreenRecorder
        this.screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
                new org.monte.media.Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new org.monte.media.Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                        QualityKey, 1.0f,
                        KeyFrameIntervalKey, 15 * 60),
                new org.monte.media.Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, file, fileName);
        this.screenRecorder.start();
    }

    public void stopRecording() throws Exception {
        if (this.screenRecorder != null) {
            this.screenRecorder.stop();
        }
    }

    public void takeScreenshot(WebDriver driver, String fileName) {
        try {
            File folder = new File("./screenshots/");
            if (!folder.exists()) folder.mkdirs();
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            FileUtils.copyFile(scrFile, new File("./screenshots/" + fileName + "_" + timestamp + ".png"));
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}

/**
 * Custom ScreenRecorder to allow naming the file specifically.
 */
class SpecializedScreenRecorder extends ScreenRecorder {
    private String name;

    public SpecializedScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                                     Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(org.monte.media.Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        // Explicitly call the Monte Registry here:
        String extension = org.monte.media.Registry.getInstance().getExtension(fileFormat);

        return new File(movieFolder, name + "-" + dateFormat.format(new Date()) + "." + extension);
    }
}