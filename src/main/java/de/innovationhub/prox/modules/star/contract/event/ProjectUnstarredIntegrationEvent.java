package de.innovationhub.prox.modules.star.contract.event;

import de.innovationhub.prox.commons.buildingblocks.IntegrationEvent;
import java.util.UUID;

public record ProjectUnstarredIntegrationEvent(
    UUID userId,
    UUID projectId
) implements IntegrationEvent {

}
