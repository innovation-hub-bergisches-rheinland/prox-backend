package de.innovationhub.prox.modules.commons.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

@MappedSuperclass
public abstract class AggregateRoot {

  private final transient @Transient List<DomainEvent> domainDomainEvents = new ArrayList<>();
  @DomainEvents
  @Transient
  public List<DomainEvent> getDomainEvents() {
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
