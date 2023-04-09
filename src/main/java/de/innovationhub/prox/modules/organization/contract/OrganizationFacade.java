package de.innovationhub.prox.modules.organization.contract;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationFacade {
    Optional<OrganizationView> get(UUID id);
    List<OrganizationView> findAllWithAnyTags(Collection<UUID> tags);
    List<OrganizationView> findAllByIds(Collection<UUID> ids);
}
