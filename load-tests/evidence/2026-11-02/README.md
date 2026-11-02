# JMeter CLI verification — 2 November 2026

Current three-sampler JMX: 3 samples, 2 passed, 1 failed. Home HTTP 403; laptop and empty-result search HTTP 200 with response-body assertions passed. This summary contains only sample labels, response codes, success flags and elapsed milliseconds. It is not a full JTL or a capacity benchmark. Raw results and the HTML dashboard remain ignored under `load-tests/results/`.

The launcher originally miscounted multiline CSV assertion messages. The corrected parser reports 3 samples and 1 failure for the same JTL, matching the dashboard. JMX SHA-256: `4b7a5899aacd414d612ce1cd212b9f631ce537a1cb6353fa95b7321fee397653`.
