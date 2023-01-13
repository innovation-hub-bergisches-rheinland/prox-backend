package de.innovationhub.prox.modules.user.domain.event;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record ProxUserStarredProject(
    UUID userId,
    UUID projectId
) implements DomainEvent {

}