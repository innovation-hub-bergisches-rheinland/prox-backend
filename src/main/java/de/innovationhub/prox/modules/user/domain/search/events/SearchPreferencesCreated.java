package de.innovationhub.prox.modules.user.domain.search.events;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.search.SearchPreferences;
import java.util.UUID;

// As long as the event is not used, it is not necessary to include additional information
public record SearchPreferencesCreated(
    UUID id,
    UUID userId
) implements DomainEvent {
}
