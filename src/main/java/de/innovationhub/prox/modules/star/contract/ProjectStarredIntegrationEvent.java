package de.innovationhub.prox.modules.star.contract;

import de.innovationhub.prox.commons.buildingblocks.IntegrationEvent;
import java.util.UUID;

public record ProjectStarredIntegrationEvent(
    UUID userId,
    UUID projectId
) implements IntegrationEvent {

}
