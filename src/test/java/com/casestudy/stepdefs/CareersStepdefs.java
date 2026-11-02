package com.casestudy.stepdefs;

import com.casestudy.context.BrowserContext;
import com.casestudy.ui.pages.CareersPage;
import com.casestudy.ui.pages.HomePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import java.util.List;

public final class CareersStepdefs {
    private final BrowserContext browser;
    private CareersPage careersPage;

    public CareersStepdefs(BrowserContext browser) {
        this.browser = browser;
    }

    private CareersPage getCareersPage() {
        if (careersPage == null) {
            careersPage = new CareersPage(browser.getDriver());
        }
        return careersPage;
    }

    @When("^I open the '(.*)' positions$")
    public void whenIOpenTheDepartmentPositions(String department) {
        getCareersPage().openTeamJobs(department);
    }

    @Then("^the application form should belong to the selected job$")
    public void thenTheApplicationFormShouldBelongToTheSelectedJob() {
        getCareersPage().assertApplicationMatchesSelectedJob();
    }

    @Given("^I open the Insider One home page$")
    public void givenIOpenTheHomePage() {
        new HomePage(browser.getDriver()).open();
    }

    @Then("^all main home page blocks should be loaded$")
    public void thenAllMainHomePageBlocksShouldBeLoaded() {
        new HomePage(browser.getDriver()).assertMainBlocksLoaded();
    }

    @Given("^I open the careers page$")
    public void givenIOpenTheCareersPage() {
        getCareersPage().open();
    }

    @When("^I expand all teams$")
    public void whenIExpandAllTeams() {
        getCareersPage().expandTeams();
    }

    @When("^I collapse the teams$")
    public void whenICollapseTheTeams() {
        getCareersPage().collapseTeams();
    }

    @Then("^every team card should be listed once with its Lever positions link$")
    public void thenEveryTeamCardShouldBeListedOnceWithItsLeverPositionsLink() {
        getCareersPage().assertExpandedTeamCards();
    }

    @Then("^only the initially visible team cards should remain$")
    public void thenOnlyTheInitiallyVisibleTeamCardsShouldRemain() {
        getCareersPage().assertOnlyInitialTeamsVisible();
    }

    @Then("^the same team cards should be visible again$")
    public void thenTheSameTeamCardsShouldBeVisibleAgain() {
        getCareersPage().assertSameTeamsVisibleAgain();
    }

    @Then("^every listed job should match:$")
    public void thenEveryListedJobShouldMatch(DataTable table) {
        Map<String, String> expectedJobDetails = table.asMap(String.class, String.class);
        if (!expectedJobDetails.keySet().containsAll(List.of("position", "department", "location"))) {
            throw new IllegalArgumentException("Job expectations must include position, department and location");
        }
        getCareersPage().assertJobs(
                expectedJobDetails.get("position"),
                expectedJobDetails.get("department"),
                expectedJobDetails.get("location")
        );
    }

    @When("^I click Apply for the first listed job$")
    public void whenIClickApplyForTheFirstListedJob() {
        getCareersPage().applyForFirstJob();
    }

    @Then("^the Lever application form should be displayed$")
    public void thenTheLeverApplicationFormShouldBeDisplayed() {
        getCareersPage().assertApplicationForm();
    }
}
