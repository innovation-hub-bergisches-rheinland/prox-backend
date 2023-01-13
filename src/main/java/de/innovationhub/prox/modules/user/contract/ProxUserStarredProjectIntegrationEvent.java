package de.innovationhub.prox.modules.user.contract;

import de.innovationhub.prox.modules.commons.application.IntegrationEvent;
import java.util.UUID;

public record ProxUserStarredProjectIntegrationEvent(
    UUID userId,
    UUID projectId
) implements IntegrationEvent {

}
