package de.innovationhub.prox.modules.commons.application.event;

import de.innovationhub.prox.modules.commons.contract.IntegrationEvent;
import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.Collection;

/**
 * Event Publisher interface. Publishes events once they have been happened. Publishing can be done
 * synchronously or asynchronously using a in-process message bus or a remote message broker that
 * depends on the implementation.
 */
public interface IntegrationEventPublisher {

  <EVENT extends IntegrationEvent> void publish(EVENT event);

  default <EVENT extends IntegrationEvent> void publish(Collection<EVENT> events) {
    events.forEach(this::publish);
  }
}
