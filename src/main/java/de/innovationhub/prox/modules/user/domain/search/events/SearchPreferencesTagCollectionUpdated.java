package de.innovationhub.prox.modules.user.domain.search.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import java.util.UUID;

public record SearchPreferencesTagCollectionUpdated(
    UUID id,
    UUID tagCollectionId
) implements DomainEvent {
}
