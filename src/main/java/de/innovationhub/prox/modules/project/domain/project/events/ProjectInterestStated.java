package de.innovationhub.prox.modules.project.domain.project.events;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.project.domain.project.InterestedUser;
import java.util.UUID;

public record ProjectInterestStated(
    UUID projectId,
    InterestedUser user
) implements DomainEvent {

}
