package de.innovationhub.prox.modules.project.domain.project.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import java.util.UUID;

public record ProjectUnarchived(
    UUID projectId
) implements DomainEvent {

}
