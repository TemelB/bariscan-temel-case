package com.casestudy.hooks;

import com.casestudy.context.BrowserContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UiHooks {
    private final BrowserContext context;

    public UiHooks(BrowserContext context) {
        this.context = context;
    }

    @After
    public void closeBrowser(Scenario scenario) throws Exception {
        WebDriver driver = context.getDriverOrNull();
        if (driver == null) {
            return;
        }
        try {
            if (scenario.isFailed()) {
                Path folder = Files.createTempDirectory(Path.of("target"), "ui-failure-");
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Files.write(folder.resolve("screenshot.png"), screenshot);
                Files.writeString(folder.resolve("page.html"), driver.getPageSource());
                Files.writeString(folder.resolve("url.txt"), driver.getCurrentUrl());
                scenario.attach(screenshot, "image/png", "Failure screenshot");
                scenario.attach(driver.getCurrentUrl(), "text/plain", "Failure URL");
            }
        } finally {
            driver.quit();
        }
    }
}
