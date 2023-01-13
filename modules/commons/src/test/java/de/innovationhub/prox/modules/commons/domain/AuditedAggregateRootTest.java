package de.innovationhub.prox.modules.commons.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AuditedAggregateRootTest {

  AuditedAggregateRoot aggregateRoot = new AuditedAggregateRoot() {
  };

  @Test
  void shouldReturnDomainEventsAsUnmodifiableCollection() {
    var domainEvents = aggregateRoot.getDomainEvents();
    var event = new DomainEvent() {
    };

    assertThrows(UnsupportedOperationException.class, () -> domainEvents.add(event));
  }

  @Test
  void shouldThrowOnNullEvent() {
    assertThrows(NullPointerException.class, () -> aggregateRoot.registerEvent(null));
  }

  @Test
  void shouldReturnRegisteredEventsInOrder() {
    var event1 = new DomainEvent() {};
    var event2 = new DomainEvent() {};
    var event3 = new DomainEvent() {};
    aggregateRoot.registerEvent(event1);
    aggregateRoot.registerEvent(event2);
    aggregateRoot.registerEvent(event3);

    assertThat(aggregateRoot.getDomainEvents())
        .containsExactly(event1, event2, event3);
  }

  @Test
  void shouldClearEvents() {
    var event = new DomainEvent() {};
    aggregateRoot.registerEvent(event);

    aggregateRoot.clearEvents();

    assertThat(aggregateRoot.getDomainEvents())
        .isEmpty();
  }
}