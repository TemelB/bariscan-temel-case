package com.casestudy.context;

import com.casestudy.ui.BrowserFactory;
import org.openqa.selenium.WebDriver;
import java.io.IOException;
import java.io.UncheckedIOException;

/** Scenario-scoped browser, started only when a UI step first requests it. */
public final class BrowserContext {
    private WebDriver driver;

    public WebDriver getDriver() {
        if (driver == null) {
            try {
                driver = BrowserFactory.create();
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot prepare Chrome session", e);
            }
        }
        return driver;
    }

    public WebDriver getDriverOrNull() {
        return driver;
    }
}
