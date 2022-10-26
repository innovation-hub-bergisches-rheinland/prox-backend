package de.innovationhub.prox.modules.profile.contract;

import java.util.UUID;

public interface OrganizationFacade {
    OrganizationView get(UUID id);
}
