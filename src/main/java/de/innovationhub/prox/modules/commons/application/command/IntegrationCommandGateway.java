package de.innovationhub.prox.modules.commons.application.command;

import de.innovationhub.prox.modules.commons.contract.IntegrationCommand;

/**
 * A gateway for sending integration commands.
 */
public interface IntegrationCommandGateway {
  <T extends IntegrationCommand> void send(T command);
}
