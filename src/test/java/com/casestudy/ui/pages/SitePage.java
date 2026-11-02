package com.casestudy.ui.pages;

import com.casestudy.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;

public class SitePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    public SitePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(TestConfig.get("explicitWaitSeconds"))));
    }

    public WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void click(By by) {
        WebElement element = visible(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center',behavior:'instant'})", element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void switchToNewWindow(Set<String> previousWindows) {
        for (String window : driver.getWindowHandles()) {
            if (!previousWindows.contains(window)) {
                driver.switchTo().window(window);
                return;
            }
        }
    }

    public void dismissCookies() {
        for (WebElement button : driver.findElements(By.id("wt-cli-accept-all-btn"))) {
            if (button.isDisplayed()) {
                button.click();
                wait.until(ExpectedConditions.invisibilityOf(button));
                break;
            }
        }
    }
}
