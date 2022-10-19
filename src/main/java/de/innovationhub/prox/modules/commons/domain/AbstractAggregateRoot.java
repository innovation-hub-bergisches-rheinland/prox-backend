package de.innovationhub.prox.modules.commons.domain;

import de.innovationhub.prox.modules.commons.application.event.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

public class AbstractAggregateRoot {

  private transient final @Transient List<Event> domainEvents = new ArrayList<>();

  @DomainEvents
  protected List<Event> getDomainEvents() {
    return List.copyOf(domainEvents);
  }

  protected void registerEvent(Event event) {
    Objects.requireNonNull(event);

    domainEvents.add(event);
  }

  @AfterDomainEventPublication
  protected void clearEvents() {
    domainEvents.clear();
  }
}
