package de.innovationhub.prox.modules.profile.domain.organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationPort {

  Organization save(Organization organization);

  List<Organization> getAll();

  Optional<Organization> getById(UUID id);
}
