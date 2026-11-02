package com.casestudy.ui.pages;

import com.casestudy.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.Assertions;

public final class HomePage extends SitePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(TestConfig.get("ui.baseUrl") + "/");
        visible(By.cssSelector(".homepage-hero h1"));
        dismissCookies();
        return this;
    }

    public void assertMainBlocksLoaded() {
        Assertions.assertEquals("insiderone.com", java.net.URI.create(driver.getCurrentUrl()).getHost());
        String[] blocks = {"#navigation", ".homepage-hero", ".homepage-social-proof", ".homepage-capabilities",
            ".homepage-insider-one-ai", ".homepage-channels", ".homepage-case-study", ".homepage-analyst",
            ".homepage-integrations", ".homepage-resources", ".homepage-call-to-action", "#footer"};
        for (String block : blocks) {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(block)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center',behavior:'instant'})", element);
            Assertions.assertTrue(visible(By.cssSelector(block)).isDisplayed(), "Main block not visible: " + block);
            // Scroll-triggered animations can reveal the container before its text.
            wait.withMessage("Main block text did not become visible: " + block)
                    .until(browser -> !browser.findElement(By.cssSelector(block)).getText().isBlank());
        }
    }
}
