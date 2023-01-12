package de.innovationhub.prox.modules.auth.contract;

import de.innovationhub.prox.modules.commons.application.IntegrationEvent;
import java.util.UUID;

public record ProxUserUnstarredProjectIntegrationEvent(
    UUID userId,
    UUID projectId
) implements IntegrationEvent {

}
