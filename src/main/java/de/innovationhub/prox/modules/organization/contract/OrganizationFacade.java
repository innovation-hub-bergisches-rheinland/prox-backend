package de.innovationhub.prox.modules.organization.contract;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationFacade {
    Optional<OrganizationView> get(UUID id);
}
