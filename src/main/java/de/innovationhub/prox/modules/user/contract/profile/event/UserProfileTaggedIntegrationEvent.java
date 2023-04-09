package de.innovationhub.prox.modules.user.contract.profile.event;

import java.util.Collection;
import java.util.UUID;

public record UserProfileTaggedIntegrationEvent(
    UUID profileId,
    Collection<UUID> tags
) {

}