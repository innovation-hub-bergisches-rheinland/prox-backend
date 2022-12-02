package de.innovationhub.prox.modules.profile.contract;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationFacade {
    Optional<OrganizationView> get(UUID id);
    boolean isMember(UUID organizationId, UUID userId);
}
