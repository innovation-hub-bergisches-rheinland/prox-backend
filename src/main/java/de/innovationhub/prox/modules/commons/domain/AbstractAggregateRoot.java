package de.innovationhub.prox.modules.commons.domain;

import de.innovationhub.prox.modules.commons.application.event.Event;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

public class AbstractAggregateRoot {

  protected transient List<Event> domainEvents = new ArrayList<>();

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
