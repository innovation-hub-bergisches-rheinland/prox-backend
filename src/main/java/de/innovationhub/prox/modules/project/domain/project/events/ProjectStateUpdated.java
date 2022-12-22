package de.innovationhub.prox.modules.project.domain.project.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.util.UUID;

public record ProjectStateUpdated(
    UUID projectId,
    ProjectState state
) implements DomainEvent {

}
