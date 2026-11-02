package com.casestudy.reporting;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/** Counts only scenarios selected by Cucumber, so filtered-out UI tests do not reduce the API pass rate. */
public final class ScenarioSummaryPlugin implements ConcurrentEventListener {
    private final AtomicInteger passed = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicInteger skipped = new AtomicInteger();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, event -> {
            passed.set(0);
            failed.set(0);
            skipped.set(0);
        });
        publisher.registerHandlerFor(TestCaseFinished.class, event -> {
            if (!Boolean.getBoolean("cucumber.execution.dry-run")) {
                System.out.println("CASE RESULT: " + event.getTestCase().getName() + " — " + event.getResult().getStatus());
            }
            switch (event.getResult().getStatus()) {
                case PASSED -> passed.incrementAndGet();
                case SKIPPED -> skipped.incrementAndGet();
                default -> failed.incrementAndGet();
            }
        });
        publisher.registerHandlerFor(TestRunFinished.class, event -> writeSummary());
    }

    private void writeSummary() {
        int total = passed.get() + failed.get() + skipped.get();
        String text;
        if (Boolean.getBoolean("cucumber.execution.dry-run")) {
            text = "DRY RUN: " + total + " selected scenarios checked for step matching. No live tests executed.";
        } else {
            String rate = total == 0 ? "N/A" : String.format(Locale.ROOT, "%.2f%%", 100.0 * passed.get() / total);
            text = String.format(Locale.ROOT,
                "SCENARIO SUMMARY%nSelected: %d | Passed: %d | Failed: %d | Skipped: %d%nPass rate: %s%nReport: target/cucumber-report.html",
                total, passed.get(), failed.get(), skipped.get(), rate);
        }
        System.out.println("\n" + text + "\n");
        try {
            Path folder = Path.of("target");
            Files.createDirectories(folder);
            Files.writeString(folder.resolve("scenario-summary.txt"), text + System.lineSeparator());
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write scenario summary", e);
        }
    }
}
