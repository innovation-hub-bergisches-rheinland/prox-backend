package de.innovationhub.prox.modules.commons.domain;

import de.innovationhub.prox.modules.commons.application.event.Event;
import java.io.Serializable;
import java.util.List;

public class AbstractAggregateRoot implements Serializable {
  protected transient List<Event> domainEvents;

  protected List<Event> getDomainEvents() {
    return domainEvents;
  }

  protected void registerEvent(Event event) {
    domainEvents.add(event);
  }

  protected void clearEvents() {
    domainEvents.clear();
  }
}
