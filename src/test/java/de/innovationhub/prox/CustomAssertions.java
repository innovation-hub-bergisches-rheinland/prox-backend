package de.innovationhub.prox;

import static org.awaitility.Awaitility.await;

import java.time.Duration;
import org.awaitility.core.ThrowingRunnable;

public class CustomAssertions {
  public static void assertEventually(ThrowingRunnable assertion) {
    await().atMost(Duration.ofSeconds(10)).untilAsserted(assertion);
  }
}
