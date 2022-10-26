package de.innovationhub.prox.impl;

import de.innovationhub.prox.modules.commons.contract.IntegrationEvent;
import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.commons.application.event.IntegrationEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationBusIntegrationEventPublisher implements IntegrationEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public SpringApplicationBusIntegrationEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public <T extends IntegrationEvent> void publish(T event) {
    this.eventPublisher.publishEvent(event);
  }
}
