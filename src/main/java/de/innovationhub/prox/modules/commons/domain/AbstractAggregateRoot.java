package de.innovationhub.prox.modules.commons.domain;

import de.innovationhub.prox.modules.commons.application.event.Event;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

public class AbstractAggregateRoot implements Serializable {
  protected transient List<Event> domainEvents;

  @DomainEvents
  protected List<Event> getDomainEvents() {
    return domainEvents;
  }

  protected void registerEvent(Event event) {
    domainEvents.add(event);
  }

  @AfterDomainEventPublication
  protected void clearEvents() {
    domainEvents.clear();
  }
}
