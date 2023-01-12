package de.innovationhub.prox.modules.auth.domain.event;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record ProxUserUnstarredProject(
    UUID userId,
    UUID projectId
) implements DomainEvent {

}
