# Execution and verification notes

Last reviewed: 2 November 2026. The suite contains 16 scenarios: six positive API, seven negative API and three UI. Run dates are specified separately below.

## Verification summary

| Check | Result | Scope / evidence |
| --- | --- | --- |
| Compilation and step matching | 16 scenarios matched; BUILD SUCCESS | Dry run on 2 November 2026; not live success |
| Full API selection (`@api`) | 13 selected, 13 passed, 0 failed, 0 skipped; 100% | Live run on 1 November 2026; `target/simplification-api.json`, `target/simplification-api.html` |
| Invalid-ID GET/DELETE | Both passed the current HTTP 404 assertions | The documented HTTP 400 discrepancy remains API-001 |
| Latest supplemental UI selection | 2 selected, 0 passed, 2 failed before navigation | Visible run on 2 November 2026; `target/final-review-ui.json`, `target/final-review-ui.html` |
| Candidate UI verification | Sales Operations and teams-toggle passed | Candidate-confirmed run on 1 November 2026; separate from the latest assistant attempt |
| Required QA flow | Candidate observed 0 open positions | UI-001; latest assistant attempt did not reach the site |
| Current JMeter plan | 3 samples, 2 passed, 1 failed; home HTTP 403 | CLI run on 2 November 2026; [sanitized summary](../load-tests/evidence/2026-11-02/summary.csv) |

## API

Scenarios use domain-oriented steps and Java-side HTTP/payload construction. `PetFixture` allocates an unused positive ID and prepares independently verified prerequisites. Only ID collisions are retried; unexpected service responses fail immediately. Cleanup deletes only IDs registered for creation by that scenario.

The six positive cases cover create, read, JSON update, form update, find by status and delete. The seven negative cases cover missing-ID read/delete, malformed-ID read/delete, malformed JSON create/update and missing-ID form update. Form update verifies persistence with a subsequent GET; find-by-status checks the fixture's own pet ID.

```bash
mvn test -Dcucumber.filter.tags="@api"
```

Latest live API result:

```text
Selected: 13 | Passed: 13 | Failed: 0 | Skipped: 0
Pass rate: 100.00%
BUILD SUCCESS
```

Malformed-ID cases assert the observed 404, not the Swagger-documented 400. Passing these assertions does not resolve that contract discrepancy. Request/response logs are printed and attached to Cucumber and Allure reports. Sensitive authentication and cookie headers are redacted.

## UI

The latest supplemental selection attempted the Sales Operations application flow and teams-toggle scenario in visible Chrome. Both failed with `SessionNotCreatedException: Chrome instance exited` before navigation. This is a browser-startup limitation in the assistant execution environment; it does not establish current vacancy availability or an application-level regression.

```bash
mvn test -Dcucumber.filter.tags="@availableJobApplication or @teamsToggle" -Dheadless=false
```

Separate candidate verification passed these two scenarios. The required QA scenario retains QA/Istanbul expectations and previously reached a card with zero open positions. Sales Operations is a separate alternative and does not establish that QA requirements passed. No application is submitted. Rerun the full UI selection where Chrome can start before claiming full UI success:

```bash
mvn test -Dcucumber.filter.tags="@ui" -Dheadless=false
```

## JMeter

The manually rebuilt plan uses one user, one loop and three requests: home, laptop search and an unlikely query. All three assert HTTP 200. Search requests additionally validate the laptop heading/product-card markup and the empty-result message.

The 2 November CLI run returned HTTP 403 for home and HTTP 200 for both searches; both search content checks passed. Result: 3 samples, 2 passed, 1 failed (33.33% errors). HTTP 403 is not accepted as success, and the plan is not claimed as a fully successful run or a capacity benchmark. The exact edge-security rule responsible for the block was not confirmed.

Retained evidence: [sample summary](../load-tests/evidence/2026-11-02/summary.csv) and [scope / JMX hash](../load-tests/evidence/2026-11-02/README.md). Raw JTL and dashboard remain ignored under `load-tests/results/`. The launcher parses quoted and multiline CSV records and returns failure for failed samples. The parser was checked against the same JTL and cases containing quoted commas, embedded newlines, reordered columns, and invalid/empty data.

```bash
mvn verify -Pload
# Or, with JMeter installed:
bash load-tests/run.sh
```

## Reports and review scope

Allure Cucumber adapter 2.29.1 and Maven plugin 2.15.2 (CLI 2.30.0) are configured. Results are stored under `target/allure-results/`; `mvn allure:report` generates `target/site/allure-maven-plugin/`. API traffic and UI failure attachments are included when available. Gson 2.13.2 is required by REST Assured for Map-to-JSON serialization. Allure generation was verified with 13 passed API results and their traffic attachments.

UI page objects, API fixture/transport separation and the JMeter plan were reviewed against the three task summaries. The corresponding [review records](../prompts/README.md) distinguish current review inputs from retrospective or edited descriptions. Static checks and candidate-reported results are distinguished from assistant live executions.

Generated reports, browser profiles, driver logs, raw HTML and IDE files remain Git-ignored. Known service/test-data limitations are recorded in [KNOWN_ISSUES.md](KNOWN_ISSUES.md).
