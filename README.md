# Bariscan Temel — QA Case Study

UI automation, pet CRUD API tests and a one-user n11 search test in one Java/Maven repository using Cucumber and JUnit.

**Start here:** [UI scenarios](src/test/resources/features/ui/careers.feature) · [API scenarios](src/test/resources/features/api/pet_crud.feature) · [Türkçe proje rehberi](docs/PROJE_REHBERI.md)

Java/Maven/Cucumber automation with independent API scenarios, domain-oriented BDD steps, Selenium page objects and a JMeter search plan. The current API suite passed all 13 scenarios in the latest live run. The latest assistant UI attempts could not create a Chrome session; earlier candidate runs passed Sales Operations and teams-toggle, while the required QA flow was blocked by missing vacancies. Invalid-ID scenarios assert the observed 404 behavior rather than the documented 400 contract. See [execution notes](docs/EXECUTION.md) for scope and evidence.

## Prerequisites

- JDK 21, Maven 3.9+ and Google Chrome
- Apache JMeter is optional: `mvn verify -Pload` downloads the pinned JMeter 5.6.3 through `jmeter-maven-plugin`
- Internet access to Maven Central, browser-driver downloads and the three target services

Verify with `java -version` and `mvn -version`; use `jmeter --version` only for a separately installed JMeter.
Selenium Manager resolves ChromeDriver automatically; no hard-coded local driver path is used.

## IntelliJ IDEA

Open this directory as a Maven project. Use JDK 21 and allow Maven import to finish. Dependencies and test source folders should be recognized automatically.

## Install / compile

```bash
mvn test-compile
```

## Tags and running scenarios

Tags have two roles:

- **Permanent descriptive tags:** feature groups such as `@api @pet @positive`, scenario identifiers such as `@createPet` / `@readPet`, and priorities `@p1` / `@p2`. Keep these on the feature/scenario.
- **Temporary selection:** add or move `@wip` (work in progress) to the scenario or feature currently being worked on. Do not remove its descriptive tags.

No scenario or feature has `@wip` checked in. Add it temporarily when you want to run a selected scenario; the following is an example of how to add it:

```gherkin
@api @pet @positive
Feature: Petstore pet CRUD operations

  @p1 @createPet @wip
  Scenario: Create a pet and verify it is persisted
    # Given / When / Then

  @p1 @readPet
  Scenario: Retrieve an existing pet
    # Given / When / Then
```

```bash
# Temporary selection: same command whether @wip is on a feature or a scenario
mvn test -Dcucumber.filter.tags="@wip"
```

To select **all scenarios in a feature**, place `@wip` with the permanent tags above `Feature:`. To select **one scenario**, remove `@wip` from the feature and other scenarios, then add it to the intended scenario's tag line. Permanent feature tags do not cause unrelated scenarios to run when filtering by `@wip`. If multiple places have `@wip`, all of those are selected.

```gherkin
# Entire feature selected by @wip:
@api @pet @positive @wip
Feature: Petstore pet CRUD operations
```

Other optional selections:

```bash
mvn test -Dcucumber.filter.tags="@createPet"
mvn test -Dcucumber.filter.tags="@readPet"
mvn test -Dcucumber.filter.tags="@api"
mvn test -Dcucumber.filter.tags="@ui"
mvn test
```

There are 16 scenarios: six independent positive API (create, read, JSON update, form update, find by status, delete), seven negative API and three UI. Read/Update/Delete prepare their own fixtures and do not depend on Create or a fixed shared pet ID. API cleanup acts only on records created by the scenario. Chrome starts lazily when the first UI step needs it; lifecycle handling does not depend on selection tags. No test submits a job application.

Surefire’s aggregate console summary is disabled so filtered-out scenarios do not appear as skipped cases in the terminal. The SCENARIO SUMMARY and per-case result lines describe only selected scenarios. Standard Maven XML reports may still contain discovered but filtered cases. Test failures still return a failing Maven exit code. To validate all step matches without live requests, run `mvn test -Dcucumber.execution.dry-run=true`. A dry run is not live success. Reports are overwritten per run.

In IntelliJ, use the feature/scenario gutter icon for direct execution, or run `RunCucumberTest`. Add `-Dcucumber.filter.tags="@wip"` to a runner's VM options for temporary filtering. Descriptive tags also remain available for runner-based grouping.

## Domain-oriented API scenarios

API scenarios describe pet operations rather than HTTP methods, endpoints and JSON bodies. `PetStepdefs` translates these business actions into calls to the shared REST Assured client. Payload construction stays in Java; headers, timeouts and request/response logging remain centralized. Step definitions delegate test-data preparation to `PetFixture` and API operations to `PetClient`. The fixture uses a positive random numeric ID and checks that it is unused; only collisions are retried, while unexpected service errors fail immediately.

```gherkin
Given an existing pet named 'update-fixture' with status 'available'
When I update my pet to name 'case-pet-updated' and status 'sold'
Then the response status should be 200
And the pet response should have name 'case-pet-updated' and status 'sold'
When I retrieve my pet
Then the response status should be 200
And the pet response should have name 'case-pet-updated' and status 'sold'
```

Create/read/update/delete, form updates and status searches have explicit domain steps. Negative cases describe invalid IDs or malformed JSON; the deliberately invalid raw payload is kept in Java and sent without serialization. Each scenario owns its test data, and cleanup deletes only IDs allocated for creation by that scenario. Temporary `@wip`, descriptive tags and response assertions work as before.

This design makes the feature files readable without knowing HTTP transport details. New business operations may require new Java steps, while the HTTP client remains reusable. Earlier generic HTTP-step records in `prompts/` describe development history and are superseded by the domain-step design.

## JMeter

Open `load-tests/n11-search.jmx` using File > Open in JMeter. The candidate’s manually rebuilt plan contains one thread, one loop and three HTTP samplers with pacing, cookie handling, timeouts and HTTP 200 response-code assertions plus response-body checks for laptop results and the empty-result message. Summary Report and the generated HTML dashboard provide response times, throughput and error percentage. Use the GUI to inspect/debug; use CLI for measured execution:

```bash
# No local JMeter install needed (results: target/jmeter/)
mvn verify -Pload

# When jmeter is available on PATH
bash load-tests/run.sh

# Or provide the path to the binary inside your extracted distribution
JMETER_BIN=/path/to/apache-jmeter-5.6.3/bin/jmeter bash load-tests/run.sh
```

The script writes a timestamped `results.jtl` and HTML dashboard under `load-tests/results/`, and returns a failure exit code if any sample fails. JMeter itself can exit successfully even when assertions fail, so the script checks the JTL success column.

This is an HTTP smoke/load scenario with one user, not a capacity benchmark or a browser rendering test. It does not execute JavaScript or simulate a keyboard click. See [load-test scenarios](load-tests/README.md).

## Configuration

Defaults are in `src/test/resources/config.properties`. Java system properties override them:

```bash
mvn test -Dcucumber.filter.tags="@wip" -Dheadless=false -DexplicitWaitSeconds=25
```

Chrome runs headless by default so `mvn test` works without a display (for example in CI). Use `-Dheadless=false` to watch the browser locally.

`ui.baseUrl` and `api.baseUrl` can also be overridden. The UI assertions target the assessment's live site and are not a generic site validator. Chrome is the currently implemented browser.

## Reports

After an API scenario finishes, the terminal prints its request/response exchanges (including fixture setup and cleanup): HTTP method, URL, headers, request body, response status and response body. These are also attached to the scenario in the Cucumber HTML report. Authorization, Cookie, Set-Cookie and X-API-Key values are redacted. UI scenarios use browser evidence rather than REST request logging.


After each ordinary run, the terminal prints selected/passed/failed/skipped scenario counts and a pass percentage. The same summary is saved to `target/scenario-summary.txt`. Tag-filtered scenarios are excluded from this percentage. For example, 8 passing and 2 failing selected scenarios means 80%; unselected scenarios do not enter the denominator. Dry runs are explicitly labelled and do not claim a live pass rate. The HTML report shows individual steps and failure details.


- Cucumber: `target/cucumber-report.html`, `target/cucumber.json` and `target/cucumber.xml`; Maven JUnit XML/text: `target/surefire-reports/`
- UI failure screenshot, HTML and URL: `target/ui-failure-*/` when a browser session exists
- Browser startup diagnostics: `target/chromedriver.log`
- JMeter: `load-tests/results/<run>/report/index.html`

Generated reports, browser profiles and IDE files are ignored by Git. Do not publish raw browser HTML or session data without review.

## Allure report

Allure runs alongside the existing Cucumber HTML and terminal reports. It shows scenarios, Gherkin steps, tags, durations, errors and attachments. Existing API request/response logs and UI failure screenshots are forwarded through Cucumber attachments; no separate Allure annotations or Java agent are required.

```bash
# Fresh results for the selected scenarios
mvn clean test -Dcucumber.filter.tags="@api"

# Build the HTML report without rerunning tests
mvn allure:report

# Open an interactive report server (Ctrl+C stops it)
mvn allure:serve
```

Run the report command even if the test command fails, to inspect failures. Raw results are in `target/allure-results/`; the generated report is `target/site/allure-maven-plugin/index.html`. Use `allure:serve` to view it through a local server. Maven downloads the pinned Allure CLI; no separate Allure installation is required.

Allure keeps results across runs. Use `mvn clean test ...` for a report containing only the new selection; omit `clean` intentionally when collecting multiple selections in one report. Dry runs check step matching and are not evidence of live success. Overriding `cucumber.plugin` replaces the configured plugin list; include `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm` in that override if Allure output is wanted.

Allure covers the Cucumber UI/API tests. The JMeter profile continues to generate its own dashboard.

Integration references: [Allure Cucumber-JVM](https://allurereport.org/docs/cucumberjvm/), [Allure Maven plugin](https://github.com/allure-framework/allure-maven).

## Project structure

```text
src/test/resources/features/
  ui/careers.feature         Home, QA flow, supplemental available-job flow and team toggle
  api/pet_crud.feature       Six independent positive API scenarios
  api/pet_negative.feature   Seven negative API scenarios
src/test/java/com/casestudy/
  stepdefs/                 Given / When / Then implementations
  runners/RunCucumberTest.java
  hooks/                    Failure evidence, browser close and API cleanup
  context/                  Scenario-scoped browser and API state
  fixtures/PetFixture.java  Unused IDs and independently verified pet setup
  clients/PetClient.java    HTTP requests only
  ui/pages/                 Page locators, actions and UI assertions
  config/TestConfig.java
  reporting/                Terminal scenario counts and pass percentage
src/test/resources/
  config.properties
  cucumber.properties       IntelliJ glue navigation
  junit-platform.properties Runtime glue and report configuration
load-tests/n11-search.jmx
load-tests/run.sh
load-tests/evidence/
docs/EXECUTION.md
prompts/                    Curated AI collaboration records
AI_USAGE.md
```

## Scope and current limitations

The main UI flow checks home sections, expands teams, follows QA openings, validates every visible job and opens a Lever form. It is marked `@knownIssue` because the candidate’s 1 November 2026 browser run reached a QA card showing `0 Open Positions` (UI-001). An additional AI-generated scenario (`@teamsToggle`, refined after review) expands, collapses and re-expands the teams. It checks that expanding reveals more cards without duplicates, every card has a title, an open-positions count and a Lever link (team-filtered when the team has vacancies), collapsing returns to the initially visible cards, and re-expanding restores the same cards. Its prompt history is in [UI prompt record](prompts/01-ui-teams-toggle.md).

API coverage includes create/read/update/delete persistence, form-based update with a follow-up GET, find-by-status containing the scenario’s own pet ID, reading/deleting a missing ID, malformed-ID reads/deletes, malformed JSON create/update, and form-updating a nonexistent pet. Swagger specifies 400 for malformed IDs, while the observed service returns 404. The current malformed-ID scenarios retain the candidate’s locally edited 404 expectations and therefore check observed behavior rather than enforce that part of the Swagger contract; the contract discrepancy remains documented.

On 31 October 2026, no Quality Assurance jobs existed in the inspected Lever postings. The site therefore falls back to an unfiltered jobs link. The UI test must fail that unmet prerequisite instead of passing on zero jobs or validating unrelated departments. The complete application path needs revalidation when QA vacancies exist.

## Supplemental application navigation

`@availableJobApplication @alternative @positive` mirrors the original QA scenario’s stages: home page and main blocks → careers page → expand teams → open Sales Operations positions → validate every visible listing → Apply → application form. Its explicit expectations are position containing `Messaging Operations`, department `Sales Operations`, and location `Sao Paulo, Brazil`. It also checks the form, name/email fields, selected job ID in the URL and matching job title. The [Sales Operations listing](https://jobs.lever.co/insiderone?team=Sales%20Operations) was inspected when choosing this data. No vacancy ID is hard-coded and no application is submitted. This is an alternative end-to-end flow with different test data; it does not prove the required QA/Istanbul criteria passed. After correcting CSS-driven uppercase text comparisons, the candidate reran this expanded flow successfully. Listing comparisons normalize letter case and whitespace; they still validate the expected position, department and location.

```bash
mvn test -Dcucumber.filter.tags="@availableJobApplication" -Dheadless=false
```

The QA scenario retains its original expectations. `@knownIssue` records the unavailable prerequisite; it does not mark the case passed or skip it. If vacancies return, rerun the QA scenario and reassess UI-001. The alternative also fails if no jobs are available or the application navigation is broken.

## Known issues

`@knownIssue` identifies a reproduced, documented mismatch; it does not disable a scenario or change its expected result. The API cases are two malformed pet-ID checks:

- `@readInvalidPetId`: GET `/pet/invalid-id`.
- `@deleteInvalidPetId`: DELETE `/pet/invalid-id`.

`@qualityAssuranceApplication` is also marked `@knownIssue` for the missing QA vacancy prerequisite, documented as [UI-001](docs/KNOWN_ISSUES.md#ui-001-no-quality-assurance-vacancies). This is an environment/test-data limitation, not a confirmed application defect. The teams-toggle scenario is not affected and is not labelled as a known issue.

The live Swagger contract documents HTTP 400 for an invalid ID. The service instead returns HTTP 404 with a NumberFormatException message. The current scenarios assert the observed 404 behavior following a candidate edit; passing them does not prove compliance with the documented 400 contract. Reproduction steps and evidence are in [API-001](docs/KNOWN_ISSUES.md).

```bash
# Only documented known issues
mvn test -Dcucumber.filter.tags="@knownIssue"

# All API scenarios, including known issues
mvn test -Dcucumber.filter.tags="@api"

# Explicitly exclude known issues for a separate functional run
mvn test -Dcucumber.filter.tags="@api and not @knownIssue"

# Your current WIP selection, explicitly excluding known issues
mvn test -Dcucumber.filter.tags="@wip and not @knownIssue"
```

Known issues are included by default. The reviewed copy's implicit `not @knownIssue` filter was not adopted. Exclusion must be explicit, for example `-Dcucumber.filter.tags="not @knownIssue"`; a green scoped run is not a passing full suite. New failures must be investigated before being labelled `@knownIssue`. Tags are case-sensitive: use `@teamsToggle`, not `@teamstoggle`.

## Validation status

The current suite contains 16 scenarios: six positive API, seven negative API and three UI. The latest live API run passed all 13 cases. UI execution was attempted in both headless and visible modes, but Chrome session creation failed before application steps could execute. Earlier candidate-confirmed UI results are listed separately.

| Scope | Latest result | Evidence |
| --- | --- | --- |
| Compile / Cucumber matching | 16 scenarios matched in refactor dry run; latest live runs compiled | Dry run is not live success |
| Full API selection (`@api`) | 13 selected, 13 passed, 0 failed; 100% | 1 November 2026 live run; `target/review-api.json`, `target/review-api.html` |
| Invalid-ID GET/DELETE | Both passed the current 404 assertions | The Swagger 400 discrepancy remains API-001 |
| Latest supplemental UI selection | 2 selected, 0 passed, 2 failed at Chrome session creation | 2 November 2026 visible attempt; `target/final-review-ui.json` |
| Earlier candidate Sales Operations and teams-toggle run | Both passed | Candidate-confirmed earlier run; not the latest assistant execution |
| Required QA application flow | Previously blocked by 0 open positions | UI-001; latest attempt did not reach the site |
| Current JMeter plan | 3 samples, 2 passed, 1 failed; home HTTP 403 | 2 November 2026 CLI run; `load-tests/evidence/2026-11-02/` |

The API pass rate reflects the current feature expectations, including 404 for malformed IDs; it does not establish full Swagger compliance. A passing complete UI suite is not claimed. After resolving browser startup, run:

```bash
mvn test -Dcucumber.filter.tags="@ui" -Dheadless=false
```

## AI collaboration

AI assistance was used during UI/API development and JMeter diagnosis/assertion guidance. [prompts/](prompts/README.md) contains task summaries and separate iteration records for all three tasks. The candidate manually rebuilt and configured the current JMX; diagnosis, HTML inspection and response-assertion patterns also received AI assistance. Source requests, explicitly labelled edited descriptions, retrospective context/evaluation and later-prepared task summaries are distinguished from each other. Explicitly labelled excerpts are used where only part of a larger message is reproduced; no rewritten summary is presented as a verbatim message. [AI_USAGE.md](AI_USAGE.md) contains the candidate’s four answers and distinguishes design/review contributions from assistant-implemented Java code changes.

Repository: https://github.com/TemelB/bariscan-temel-case
