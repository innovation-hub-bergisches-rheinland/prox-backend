package de.innovationhub.prox.modules.star.domain.event;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.UUID;

public record ProjectUnstarred(
    UUID collectionId,
    UUID userId,
    UUID projectId
) implements DomainEvent {

}
