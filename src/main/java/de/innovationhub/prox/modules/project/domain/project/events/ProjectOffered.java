package de.innovationhub.prox.modules.project.domain.project.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.util.List;
import java.util.UUID;

public record ProjectOffered(
    UUID projectId,
    List<Supervisor> supervisor
) implements DomainEvent {

}
