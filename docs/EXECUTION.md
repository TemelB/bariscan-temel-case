# Execution and review notes

Last verified: 1 November 2026. The current suite has 16 scenarios: six positive API, seven negative API and three UI.

## Current verification

| Check | Result | Scope / evidence |
| --- | --- | --- |
| Compile and Cucumber step matching | 16 scenarios matched in the refactor dry run; latest live selections compiled | Dry run establishes step matching only |
| Full API selection (`@api`) | 13 selected, 13 passed, 0 failed, 0 skipped; 100% | Live run; `target/review-api.json`, `target/review-api.html` |
| Invalid-ID GET/DELETE | Both passed with expected HTTP 404 | Current observed-behavior checks; Swagger 400 discrepancy remains API-001 |
| Earlier full UI selection (`@ui`), headless | 3 selected, 0 passed, 3 failed | Chrome session creation failed before page steps |
| Earlier full UI selection (`@ui`), visible | 3 selected, 0 passed, 3 failed | Same Chrome session creation failure |
| Earlier candidate Sales Operations / teams-toggle | Both passed | Earlier candidate verification, distinct from latest assistant attempts |
| Current JMeter plan | 3 samples, 2 passed, 1 failed; home HTTP 403 | 2 November 2026 CLI run; sanitized sample summary retained |

## Current implementation

- Domain-oriented pet creation, retrieval, update and delete steps; HTTP details and payload construction remain in Java.
- Six positive API scenarios: create, read, JSON update, form update, find by status, delete.
- Seven negative API scenarios: missing-ID read/delete, malformed-ID read/delete, malformed JSON create/update, missing-ID form update.
- Numeric status-code parameters, independent fixtures, cleanup limited to owned records and request/response logging.
- Home-page presence/visibility waits and normalized job comparisons.
- Expanded team-card validation, collapse and re-expansion checks.
- Headless Chrome by default; `-Dheadless=false` selects visible execution.
- Optional Maven load profile pinned to JMeter 5.6.3; manually rebuilt three-request plan for home, laptop search and an unlikely query, each asserting HTTP 200; the laptop and nonexistent-term requests additionally check their response content.
- UI/API prompt records distinguish source messages from retrospective evaluations. Load-test prompt records have been restored with actual requests for diagnosis and assertion guidance; manual JMeter configuration is distinguished from AI assistance.

Generic HTTP request steps, the custom-content-type step, unsupported-media-type scenario, Turkish-character sampler and page-2 sampler are absent from the current implementation.

## API live run

```bash
mvn -o test -Dcucumber.filter.tags="@api" -Dcucumber.plugin="pretty,com.casestudy.reporting.ScenarioSummaryPlugin,json:target/review-api.json,html:target/review-api.html"
```

```text
Selected: 13 | Passed: 13 | Failed: 0 | Skipped: 0
Pass rate: 100.00%
BUILD SUCCESS
```

Form update verifies persistence with a subsequent GET. Find-by-status checks the fixture’s own ID. Negative cases retain explicit assertions. Malformed-ID scenarios currently expect the observed 404, not the documented 400; their passing result is not a resolved contract discrepancy.

## UI live attempts

The full `@ui` selection was run in both headless and visible modes. Both attempts failed all three scenarios with `SessionNotCreatedException: Chrome instance exited`. The failure occurs before navigation, so these attempts do not establish application-level behavior, current vacancy availability or a new UI regression.

```bash
mvn -o test -Dcucumber.filter.tags="@ui" -Dheadless=true -Dcucumber.plugin="pretty,com.casestudy.reporting.ScenarioSummaryPlugin,json:target/review-ui.json,html:target/review-ui.html"
mvn -o test -Dcucumber.filter.tags="@ui" -Dheadless=false -Dcucumber.plugin="pretty,com.casestudy.reporting.ScenarioSummaryPlugin,json:target/review-ui-visible.json,html:target/review-ui-visible.html"
```

Earlier candidate runs passed Sales Operations and teams-toggle; the original QA flow reached zero open positions. Those results remain historical candidate verification and do not replace the latest execution outcome. Rerun the full UI selection in an environment where Chrome can start before claiming full UI success. No application is submitted.

## JMeter

The current manually rebuilt JMX has three requests: home, laptop search and an unlikely query. Summary Report / HTML dashboard expose response times, throughput and error percentage. The candidate reported completing manual checks. No fresh assistant-measured sampler results are claimed in this review.

Earlier five-sampler results describe a superseded plan and are not part of the current verification table. The old JTL has been removed from the working tree; it is not referenced as current evidence.

## Report handling

The review API and UI JSON/HTML reports use separate filenames and remain Git-ignored under `target/`. Their console summaries identify the selected scope. Generated browser profiles, driver logs, raw browser HTML and IDE files remain excluded from delivery. This review does not commit or push changes.


## Allure integration verification

Allure Cucumber 7 adapter 2.29.1 and Maven plugin 2.15.2 (report CLI 2.30.0) are configured. Results are stored under `target/allure-results/`; `mvn allure:report` generates `target/site/allure-maven-plugin/`. Existing Cucumber attachments appear in Allure.

The first live probe exposed a runtime serializer dependency: REST Assured requires Gson to serialize Map request bodies even without explicit Gson imports. Gson 2.13.2 was restored. The subsequent complete API run passed all 13 scenarios and produced 13 passed Allure results. Its results replaced the initial probe in the default report directory. This verification does not change the previously documented Chrome startup limitation.


## Latest manually saved JMX

The project JMX includes the renamed “03 Search for a nonexistent term” sampler, laptop heading/product-card assertions and the empty-result message assertion. XML parsing and pattern checks against the candidate-supplied saved HTML responses passed. These are static checks, not a new live JMeter run.


## Readability and responsibility review

Step definitions now delegate unused-ID lookup and prerequisite creation to `PetFixture`, and explicit pet operations to `PetClient`. UUID arithmetic was replaced with a bounded positive random long; unused-ID validation and collision-only retries remain. Scenario ID validation is centralized in `PetContext`. UI page methods reuse named team-card/text checks and a shared window-switch helper. Feature wording, tags, assertions, logging and the JMX were preserved.

Verification: 16 scenarios matched in a full dry run; the live API selection passed 13/13 after refactoring (100%). Isolated HTTP checks also verified unused-ID success, collision retry, immediate failure on HTTP 500 and the three-attempt limit; missing fixture ID access fails clearly. Evidence: `target/simplification-api.json`, `target/simplification-api.html`, `target/allure-simplification/`. UI page changes were compiled and step-matched, but no new live UI success is claimed; the Chrome startup limitation above remains.


## Task-text review — 2 November 2026

The three task summaries were explicitly supplied as inputs to a new review. UI/API implementations matched their described design, so no duplicate scenarios or cosmetic code changes were added. The exact review invocation and supplied task text/context are recorded in new UI/API/load iteration files.

Latest supplemental UI attempt (`@availableJobApplication or @teamsToggle`, visible mode): 2 selected, 0 passed, 2 failed before page navigation with Chrome SessionNotCreatedException. Evidence: `target/final-review-ui.json` and `target/final-review-ui.html`. This is not a new application-level failure or a fresh vacancy observation.

Latest current JMeter CLI run: 3 samples, 2 passed, 1 failed; home HTTP 403, both searches HTTP 200 with content checks passed. Sanitized sample summary: `load-tests/evidence/2026-11-02/summary.csv`. Raw dashboard remains under ignored `load-tests/results/`. Initial launcher output incorrectly counted multiline assertion-message lines as samples; the CSV-aware parser now reports 3/1 for the same JTL, matching dashboard statistics. Quoted commas, embedded newlines, reordered columns and invalid/empty data were checked. The JMX was not changed.

The 13/13 API live run from the readability refactor remains the latest API evidence; it was not rerun during this documentation-focused review.
