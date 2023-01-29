package de.innovationhub.prox.modules.project.domain.project.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.Collection;
import java.util.UUID;

public record ProjectTagged(
    UUID projectId,
    Collection<UUID> tags
) implements DomainEvent {

}
