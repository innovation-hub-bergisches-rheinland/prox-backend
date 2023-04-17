package de.innovationhub.prox.modules.organization.contract;

import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationFacade {
    Optional<OrganizationDto> get(UUID id);
    List<OrganizationDto> findAllByIds(Collection<UUID> ids);
}
