package de.innovationhub.prox.modules.commons.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.commons.application.event.Event;
import org.junit.jupiter.api.Test;

class AbstractAggregateRootTest {
  AbstractAggregateRoot aggregateRoot = new AbstractAggregateRoot() {};

  @Test
  void shouldReturnDomainEventsAsUnmodifiableCollection() {
    var domainEvents = aggregateRoot.getDomainEvents();
    var event = new Event() {
    };

    assertThrows(UnsupportedOperationException.class, () -> domainEvents.add(event));
  }

  @Test
  void shouldThrowOnNullEvent() {
    assertThrows(NullPointerException.class, () -> aggregateRoot.registerEvent(null));
  }

  @Test
  void shouldReturnRegisteredEventsInOrder() {
    var event1 = new Event() {};
    var event2 = new Event() {};
    var event3 = new Event() {};
    aggregateRoot.registerEvent(event1);
    aggregateRoot.registerEvent(event2);
    aggregateRoot.registerEvent(event3);

    assertThat(aggregateRoot.getDomainEvents())
        .containsExactly(event1, event2, event3);
  }

  @Test
  void shouldClearEvents() {
    var event = new Event() {};
    aggregateRoot.registerEvent(event);

    aggregateRoot.clearEvents();

    assertThat(aggregateRoot.getDomainEvents())
        .isEmpty();
  }
}