package de.innovationhub.prox.modules.commons.application.command;

import de.innovationhub.prox.modules.commons.contract.IntegrationCommand;

public interface IntegrationCommandHandler<T extends IntegrationCommand> {
  void handleCommand(T command);
}
