package de.innovationhub.prox.modules.project.domain.project.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.UUID;

public record ProjectCreated(
    UUID projectId
) implements DomainEvent {

}
