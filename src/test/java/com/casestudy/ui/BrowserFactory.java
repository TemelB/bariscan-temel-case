package com.casestudy.ui;

import com.casestudy.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public final class BrowserFactory {
    private BrowserFactory() {}

    public static WebDriver create() throws IOException {
        if (!TestConfig.get("browser").equals("chrome")) {
            throw new IllegalArgumentException("Only chrome is supported");
        }
        var options = new ChromeOptions();
        if (Boolean.parseBoolean(TestConfig.get("headless"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,1000", "--lang=en-US");
        Files.createDirectories(Path.of("target"));
        options.addArguments("--user-data-dir=" + Files.createTempDirectory(Path.of("target"), "chrome-profile-").toAbsolutePath());
        var service = new ChromeDriverService.Builder().withVerbose(true)
            .withLogFile(Path.of("target", "chromedriver.log").toFile()).build();
        WebDriver driver = new ChromeDriver(service, options);
        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            return driver;
        } catch (RuntimeException e) {
            driver.quit();
            throw e;
        }
    }
}
