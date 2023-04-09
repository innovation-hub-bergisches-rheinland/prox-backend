package de.innovationhub.prox.modules.project.contract.event;

import java.util.Collection;
import java.util.UUID;

public record ProjectTaggedIntegrationEvent(
    UUID projectId,
    Collection<UUID> tags
) {

}
