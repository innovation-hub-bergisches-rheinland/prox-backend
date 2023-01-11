package de.innovationhub.prox.modules.project.contract;

import java.util.Collection;
import java.util.UUID;

public record ProjectTaggedIntegrationEvent(
    UUID projectId,
    Collection<UUID> tags
) {

}
