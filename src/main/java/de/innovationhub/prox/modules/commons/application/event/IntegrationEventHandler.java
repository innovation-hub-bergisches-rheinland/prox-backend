package de.innovationhub.prox.modules.commons.application.event;

import de.innovationhub.prox.modules.commons.contract.IntegrationEvent;

public interface IntegrationEventHandler <T extends IntegrationEvent> {
  void handleEvent(T event);
}
