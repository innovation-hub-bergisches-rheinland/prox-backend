package de.innovationhub.prox.modules.project.domain.project.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.Collection;
import java.util.UUID;

public record ProjectTagCollectionUpdated(
    UUID projectId,
    UUID tagCollectionId
) implements DomainEvent {

}
