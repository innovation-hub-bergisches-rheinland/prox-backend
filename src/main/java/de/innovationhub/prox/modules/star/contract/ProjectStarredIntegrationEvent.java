package de.innovationhub.prox.modules.star.contract;

import de.innovationhub.prox.modules.commons.application.IntegrationEvent;
import java.util.UUID;

public record ProjectStarredIntegrationEvent(
    UUID userId,
    UUID projectId
) implements IntegrationEvent {

}