package de.innovationhub.prox.modules.user.contract.star;

import de.innovationhub.prox.modules.commons.application.IntegrationEvent;
import java.util.UUID;

public record ProjectUnstarredIntegrationEvent(
    UUID userId,
    UUID projectId
) implements IntegrationEvent {

}
