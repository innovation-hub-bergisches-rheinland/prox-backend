package de.innovationhub.prox.impl;

import de.innovationhub.prox.modules.commons.application.event.Event;
import de.innovationhub.prox.modules.commons.application.event.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationBusEventPublisher implements EventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public SpringApplicationBusEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public <T extends Event> void publish(T event) {
    this.eventPublisher.publishEvent(event);
  }
}
