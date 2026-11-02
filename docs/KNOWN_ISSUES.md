# Known issues

The current API suite contains 13 scenarios and passed all 13 in the latest live run on 1 November 2026. Malformed-ID scenarios assert the observed 404; API-001 remains the upstream contract discrepancy, not a currently failing assertion. The latest full UI attempts failed at Chrome startup; earlier vacancy observations are distinguished below.

`@knownIssue` documents a reproduced limitation; it does not skip a test or change its expected result. All unfiltered runs include these scenarios. Explicit exclusions must be reported as limited-scope runs.

## API-001: malformed pet ID returns 404 instead of documented 400

Observed: 1 November 2026, public Swagger Petstore v2. The [Swagger specification](https://petstore.swagger.io/v2/swagger.json) documents HTTP 400 for an invalid ID and HTTP 404 for a missing pet on GET and DELETE `/pet/{petId}`. The parameter is an integer/int64. Sending `invalid-id` returns HTTP 404 with a NumberFormatException message.

Affected cases: `@readInvalidPetId` and `@deleteInvalidPetId`. Both currently assert the observed HTTP 404 and passed in the latest 13-scenario live API run. They do not enforce the documented HTTP 400 response. The retained @contract / @knownIssue classification identifies the discrepancy; passing these assertions does not resolve it.

```bash
curl -i https://petstore.swagger.io/v2/pet/invalid-id
curl -i -X DELETE https://petstore.swagger.io/v2/pet/invalid-id
```

Observed GET body:

```json
{"code":404,"type":"unknown","message":"java.lang.NumberFormatException: For input string: \"invalid-id\""}
```

This repository cannot fix the upstream routing/error-handling mismatch. Latest API execution: 13 selected, 13 passed, 0 failed; both malformed-ID cases are included with their current 404 expectations.

## UI-001: no Quality Assurance vacancies

Observed: 1 November 2026, candidate's visible-browser run of `@qualityAssuranceApplication`. Home-page checks, careers navigation and team expansion passed; the QA card showed `0 Open Positions`.

This is an unavailable test-data prerequisite, not a confirmed application defect. The required QA/Istanbul checks and application navigation cannot be completed without a qualifying vacancy. The original scenario keeps its expectations and fails on zero vacancies; unrelated jobs are not automatically substituted.

The separate `@availableJobApplication @alternative @positive` scenario mirrors the full navigation using Sales Operations. It checks position containing `Messaging Operations`, department `Sales Operations`, location `Sao Paulo, Brazil`, and the selected job's application form. The candidate confirmed it passed after the rendered-text comparison fix. This is alternative coverage, not evidence that the required QA/Istanbul criteria passed. The expanded `@teamsToggle` also passed in that earlier candidate scoped UI run; its seven successful steps were verified in Cucumber JSON. The latest assistant full UI attempts, in both headless and visible modes, failed at Chrome session creation before site navigation; they did not recheck vacancies or those application outcomes. It is not marked as a known issue. No application is submitted.

Recheck QA vacancies before delivery. When they return, rerun the original scenario and remove its classification only after confirming the required flow.

## Commands

```bash
# All known issues, without suppressing their failures
mvn test -Dcucumber.filter.tags="@knownIssue"

# Full API and UI selections
mvn test -Dcucumber.filter.tags="@api"
mvn test -Dcucumber.filter.tags="@ui" -Dheadless=false

# Explicitly limited selections
mvn test -Dcucumber.filter.tags="@api and not @knownIssue"
mvn test -Dcucumber.filter.tags="@ui and not @knownIssue" -Dheadless=false
```

An excluded known issue is not a resolved issue. See `EXECUTION.md` for the latest results. The separate JMeter home-page HTTP 403 limitation is documented in `load-tests/README.md`.
