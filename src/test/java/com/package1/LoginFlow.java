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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class LoginFlow {

    private ScreenRecorder screenRecorder;

    @Test
    public void Login() throws Exception {
        System.out.println("--- STEP 1: INITIALIZING TEST ---");

        // Now these functions are defined below!
        startRecording("Login_Flow");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            driver.get("https://test.web.cashbook.in/login");

            WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phoneNumber")));
            phone.sendKeys("+911231231235");
            Thread.sleep(5000);

            wait.until(ExpectedConditions.elementToBeClickable(By.id("submitPhoneNumber"))).click();

            WebElement otp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("code")));
            otp.sendKeys("123456");

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Verify']"))).click();
            Thread.sleep(3000);

        } catch (Exception e) {
            takeScreenshot(driver, "Failure_Log");
            throw e;
        } finally {
            driver.quit();
            stopRecording();
        }
    }

    // --- DEFINING THE MISSING FUNCTIONS ---

    public void startRecording(String methodName) throws Exception {
        File file = new File(System.getProperty("user.dir") + File.separator + "recordings");
        if (!file.exists()) file.mkdirs();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        this.screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
                // Use QuickTime MIME type
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_QUICKTIME),
                // Use standard Video format settings
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_QUICKTIME_ANIMATION,
                        CompressorNameKey, ENCODING_QUICKTIME_ANIMATION,
                        DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                        QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                        FrameRateKey, Rational.valueOf(30)),
                null, file, methodName);

        this.screenRecorder.start();
    }

    public void stopRecording() throws Exception {
        this.screenRecorder.stop();
        System.out.println("Recording stopped.");
    }

    public void takeScreenshot(WebDriver driver, String fileName) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        FileUtils.copyFile(scrFile, new File("./screenshots/" + fileName + "_" + timestamp + ".png"));
        System.out.println("Screenshot saved.");
    }
}
class SpecializedScreenRecorder extends ScreenRecorder {
    private String name;

    public SpecializedScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                                     Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        }
        // Explicitly naming the file .mp4
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return new File(movieFolder, name + "_" + dateFormat.format(new Date()) + ".mp4");
    }
}