package de.innovationhub.prox.impl;

import de.innovationhub.prox.modules.commons.application.command.IntegrationCommandGateway;
import de.innovationhub.prox.modules.commons.application.event.IntegrationEventPublisher;
import de.innovationhub.prox.modules.commons.contract.IntegrationCommand;
import de.innovationhub.prox.modules.commons.contract.IntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationBusIntegrationCommandGateway implements IntegrationCommandGateway {

  private final ApplicationEventPublisher eventPublisher;

  public SpringApplicationBusIntegrationCommandGateway(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public <T extends IntegrationCommand> void send(T command) {
    this.eventPublisher.publishEvent(command);
  }
}
