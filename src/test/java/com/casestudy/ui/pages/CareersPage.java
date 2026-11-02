package com.casestudy.ui.pages;

import com.casestudy.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.jupiter.api.Assertions;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class CareersPage extends SitePage {
    private static final By TOGGLE = By.cssSelector("#open-roles .see-more");
    private static final By QA_CARD = By.cssSelector("#open-roles [data-department='Quality Assurance']");
    private static final By TEAM_CARDS = By.cssSelector("#open-roles [data-department]");
    private List<String> initiallyVisibleTeams = List.of();
    private List<String> expandedTeams = List.of();
    private String selectedJobPath;
    private String selectedJobTitle;
    public CareersPage(WebDriver driver) {
        super(driver);
    }

    public CareersPage open() {
        driver.get(TestConfig.get("ui.baseUrl") + "/careers/#open-roles");
        visible(TOGGLE);
        dismissCookies();
        wait.until(browser -> !visibleTeamNames().isEmpty());
        initiallyVisibleTeams = visibleTeamNames();
        return this;
    }

    public void expandTeams() {
        click(TOGGLE);
        wait.until(ExpectedConditions.attributeToBe(TOGGLE, "aria-expanded", "true"));
        visible(QA_CARD);
    }

    public void collapseTeams() {
        click(TOGGLE);
        wait.until(ExpectedConditions.attributeToBe(TOGGLE, "aria-expanded", "false"));
    }

    private List<String> visibleTeamNames() {
        return driver.findElements(TEAM_CARDS).stream()
                .filter(WebElement::isDisplayed)
                .map(card -> card.getAttribute("data-department"))
                .toList();
    }

    public void assertExpandedTeamCards() {
        List<WebElement> cards = driver.findElements(TEAM_CARDS).stream()
                .filter(WebElement::isDisplayed)
                .toList();
        List<String> names = cards.stream().map(card -> card.getAttribute("data-department")).toList();
        Assertions.assertTrue(
                names.size() > initiallyVisibleTeams.size(),
                "Expanding should reveal more team cards. Before: " + initiallyVisibleTeams + ", after: " + names
        );
        Assertions.assertEquals(Set.copyOf(names).size(), names.size(), "Team cards must not be duplicated: " + names);

        cards.forEach(this::assertTeamCard);
        expandedTeams = names;
    }

    private void assertTeamCard(WebElement card) {
        String department = card.getAttribute("data-department");
        // Display titles may differ from the Lever team name (e.g. Human Resources / People and Culture).
        Assertions.assertFalse(
                card.findElement(By.cssSelector("h3")).getText().isBlank(),
                department + " team card should show a title"
        );

        WebElement link = card.findElement(By.cssSelector("a"));
        wait.until(browser -> {
            String href = link.getAttribute("href");
            return isPositionsLink(href);
        });
        String openings = link.getText().trim();
        Assertions.assertTrue(
                openings.matches("\\d+ Open Positions?"),
                department + " card should show an open positions count but was: " + openings
        );
        // Teams without vacancies fall back to the unfiltered Lever list (UI-001); others must filter by team.
        if (!openings.startsWith("0 ")) {
            Assertions.assertTrue(
                    hasTeamFilter(link.getAttribute("href"), department),
                    department + " positions link should filter by team: " + link.getAttribute("href")
            );
        }
    }

    public void assertOnlyInitialTeamsVisible() {
        waitForVisibleTeams(initiallyVisibleTeams, "Collapsed teams should match the initially visible cards");
    }

    public void assertSameTeamsVisibleAgain() {
        Assertions.assertFalse(expandedTeams.isEmpty(), "Expand the teams once before comparing");
        waitForVisibleTeams(expandedTeams, "Re-expanded teams should match the first expansion");
    }

    private void waitForVisibleTeams(List<String> expected, String message) {
        try {
            wait.until(browser -> visibleTeamNames().equals(expected));
        } catch (TimeoutException exception) {
            Assertions.assertEquals(expected, visibleTeamNames(), message);
        }
    }

    private static boolean hasTeamFilter(String href, String department) {
        String query = URI.create(href).getRawQuery();
        return query != null && java.util.Arrays.stream(query.split("&"))
                .map(parameter -> URLDecoder.decode(parameter, StandardCharsets.UTF_8))
                .anyMatch(parameter -> parameter.equals("team=" + department));
    }

    private static boolean isPositionsLink(String href) {
        if (href == null) {
            return false;
        }
        URI uri = URI.create(href);
        return "https".equals(uri.getScheme())
                && "jobs.lever.co".equals(uri.getHost())
                && ("/insiderone".equals(uri.getPath()) || "/insiderone/".equals(uri.getPath()));
    }

    public void openTeamJobs(String department) {
        By link = By.cssSelector("#open-roles [data-department='" + department + "'] a");
        wait.withMessage("Positions link was not populated for department: " + department)
                .until(browser -> {
                    String href = browser.findElement(link).getAttribute("href");
                    return isPositionsLink(href);
                });

        String openings = driver.findElement(link).getText();
        Assertions.assertFalse(
                openings.matches("(?s)^0\\s+Open.*"),
                department + " reports zero open positions; required jobs/Apply flow unavailable. Text: " + openings
        );

        Set<String> previousWindows = driver.getWindowHandles();
        click(link);
        wait.until(browser -> browser.getWindowHandles().size() > previousWindows.size()
                || browser.getCurrentUrl().contains("jobs.lever.co"));
        switchToNewWindow(previousWindows);
        wait.until(ExpectedConditions.urlContains("jobs.lever.co/insiderone"));
        Assertions.assertTrue(
                hasTeamFilter(driver.getCurrentUrl(), department),
                "Positions URL did not preserve department filter: " + driver.getCurrentUrl()
        );
    }

    private List<WebElement> visibleJobs() {
        return wait.until(d -> {
            var cards = d.findElements(By.cssSelector(".posting"));
            var shown = cards.stream().filter(WebElement::isDisplayed).toList();
            return shown.isEmpty() ? null : shown;
        });
    }

    public void assertJobs(String position, String department, String location) {
        for (WebElement job : visibleJobs()) {
            String actualPosition = job.findElement(By.cssSelector(".posting-title h5")).getText();
            String actualDepartment = job.findElement(By.xpath(
                    "ancestor::div[contains(concat(' ',normalize-space(@class),' '),' postings-group ')][1]"
            )).findElement(By.cssSelector(".posting-category-title")).getText();
            String actualLocation = job.findElement(By.cssSelector(".location")).getText();

            assertTextContains("Position", actualPosition, position);
            assertTextContains("Department", actualDepartment, department);
            assertTextContains("Location", actualLocation, location);
        }
    }

    private void assertTextContains(String field, String actual, String expected) {
        Assertions.assertTrue(
                normalizeText(actual).contains(normalizeText(expected)),
                field + " should contain '" + expected + "' but was '" + actual + "'"
        );
    }

    private static String normalizeText(String text) {
        // Lever applies uppercase styling to category labels; compare their meaning.
        return text.replace('\u00a0', ' ').trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    public void applyForFirstJob() {
        WebElement job = visibleJobs().getFirst();
        WebElement apply = job.findElement(By.cssSelector(".posting-btn-submit"));
        Set<String> before = driver.getWindowHandles();
        String detailUrl = apply.getAttribute("href");
        selectedJobPath = URI.create(detailUrl).getPath();
        selectedJobTitle = job.findElement(By.cssSelector(".posting-title h5")).getText().trim();
        Assertions.assertFalse(selectedJobTitle.isBlank(), "Selected job title must not be blank");
        apply.click();
        wait.until(d -> d.getWindowHandles().size() > before.size() || d.getCurrentUrl().equals(detailUrl));
        switchToNewWindow(before);
        // Current Lever listing Apply opens job details; its second Apply opens the application form.
        if (!URI.create(driver.getCurrentUrl()).getPath().endsWith("/apply")) {
            click(By.cssSelector("a.postings-btn[href$='/apply']"));
        }
        wait.until(browser -> {
            URI url = URI.create(browser.getCurrentUrl());
            return "jobs.lever.co".equals(url.getHost()) && url.getPath().endsWith("/apply");
        });
    }

    public void assertApplicationForm() {
        Assertions.assertEquals("jobs.lever.co", URI.create(driver.getCurrentUrl()).getHost());
        Assertions.assertTrue(URI.create(driver.getCurrentUrl()).getPath().endsWith("/apply"));
        visible(By.cssSelector("form"));
        visible(By.cssSelector("input[name='name']"));
        visible(By.cssSelector("input[name='email']"));
    }

    public void assertApplicationMatchesSelectedJob() {
        Assertions.assertNotNull(selectedJobPath, "Select a job before checking its application form");
        Assertions.assertEquals(
                selectedJobPath + "/apply",
                URI.create(driver.getCurrentUrl()).getPath(),
                "Application URL should preserve the selected job ID"
        );
        Assertions.assertEquals(
                selectedJobTitle,
                visible(By.cssSelector(".posting-header h2")).getText().trim(),
                "Application form should show the selected job title"
        );
    }
}
