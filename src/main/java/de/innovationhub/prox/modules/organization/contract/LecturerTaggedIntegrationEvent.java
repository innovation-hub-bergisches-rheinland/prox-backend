package de.innovationhub.prox.modules.profile.contract;

import java.util.Collection;
import java.util.UUID;

public record LecturerTaggedIntegrationEvent(
    UUID lecturerId,
    Collection<UUID> tags
) {

}
