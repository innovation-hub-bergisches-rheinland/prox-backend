package de.innovationhub.prox.modules.commons.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

public class AbstractAggregateRoot {

  private transient final @Transient List<DomainEvent> domainDomainEvents = new ArrayList<>();

  @DomainEvents
  protected List<DomainEvent> getDomainEvents() {
    return List.copyOf(domainDomainEvents);
  }

  protected void registerEvent(DomainEvent domainEvent) {
    Objects.requireNonNull(domainEvent);

    domainDomainEvents.add(domainEvent);
  }

  @AfterDomainEventPublication
  protected void clearEvents() {
    domainDomainEvents.clear();
  }
}
