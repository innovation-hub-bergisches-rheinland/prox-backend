package de.innovationhub.prox.modules.user.contract.user;

import java.util.Collection;
import java.util.UUID;

public record UserProfileTaggedIntegrationEvent(
    UUID profileId,
    Collection<UUID> tags
) {

}
