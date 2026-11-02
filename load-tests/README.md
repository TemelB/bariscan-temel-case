# n11 search: manually rebuilt one-user plan

The current `n11-search.jmx` is the candidate’s manually rebuilt and saved JMeter plan. It replaces the earlier five-sampler version. It has one user, one loop, one-second pacing, cookies, redirect following, 15-second connection and 30-second response timeouts. Both View Results Tree and Summary Report are included for inspection.

| ID | Request | Current assertion |
| --- | --- | --- |
| LOAD-01 | GET `/` — header search module page | HTTP status equals 200 |
| LOAD-02 | GET `/arama?q=laptop` | HTTP 200; laptop search heading and product-card markup |
| LOAD-03 | GET `/arama?q=qaAutomationNotResult` | HTTP 200; rendered “Aradığını bulamadık.” message |

## Measurements

The plan sends three HTTP requests with one user and one iteration. HTTP 200 assertions validate response status. Separate response-body assertions validate the laptop heading and product-card markup, and the empty-result message for the nonexistent term. Both content assertions use Contains with Main sample only. Use Summary Report or the generated HTML dashboard to inspect sample counts, response times, throughput and error percentage.

## Run

From the repository root:

```bash
mvn verify -Pload
```

The Maven profile downloads JMeter 5.6.3, skips Cucumber tests, runs the JMX and generates CSV results and an HTML dashboard under `target/jmeter/`. Failed samples cause the results goal to fail.

Alternatively, with JMeter installed:

```bash
bash load-tests/run.sh
# Or:
JMETER_BIN=/path/to/apache-jmeter-5.6.3/bin/jmeter bash load-tests/run.sh
```

The script creates a timestamped result directory and returns failure when samples fail. Its CSV parser handles quoted commas and multiline assertion messages. For GUI inspection, open `load-tests/n11-search.jmx` with File > Open and press Start. Disable debugging listeners before any larger run.

## Validation and historical evidence

The current three-sampler plan was run through the CLI on 2 November 2026: 3 samples, 2 passed, 1 failed. The home request returned HTTP 403; laptop and empty-result search returned HTTP 200 with their content assertions passing. A [sanitized summary](evidence/2026-11-02/summary.csv) is retained with [scope and plan hash](evidence/2026-11-02/README.md). The launcher returns a failing exit code for the failed sample. Earlier five-sampler results belong to a superseded plan.

Earlier JMeter home requests received a Cloudflare HTTP 403 block page while browser navigation succeeded. Automated-request protection may explain why JMeter and browser requests were treated differently, even with one user. The exact responsible rule was not confirmed, and no specific missing mandatory HTTP header was established. The earlier “Header search field missing” message referred to the HTML search field, not a missing HTTP request header. The current status assertions also reject HTTP 403.

## Authorship and prompt records

I manually rebuilt the current JMeter test plan and applied its settings in the JMeter GUI. I used AI assistance to investigate the HTTP 403 response, inspect the returned HTML and select the response-assertion patterns. I applied and checked those assertions myself; the current plan is therefore manually assembled with AI-assisted diagnosis and assertion guidance. Subsequent review also used AI to correct multiline CSV result counting in the launcher.

Earlier versions also involved AI assistance. The actual requests, context, output evaluations and iteration notes are documented in the [load-test collaboration records](../prompts/03-load-test-collaboration.md) and indexed in [prompts/README.md](../prompts/README.md).
