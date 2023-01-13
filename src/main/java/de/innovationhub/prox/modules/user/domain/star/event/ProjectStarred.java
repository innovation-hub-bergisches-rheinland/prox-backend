package de.innovationhub.prox.modules.user.domain.star.event;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record ProjectStarred(
    UUID collectionId,
    UUID userId,
    UUID projectId
) implements DomainEvent {

}
