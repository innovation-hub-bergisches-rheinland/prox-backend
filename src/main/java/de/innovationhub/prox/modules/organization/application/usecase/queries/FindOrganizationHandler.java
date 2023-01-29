package de.innovationhub.prox.modules.organization.application.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindOrganizationHandler {
  private final OrganizationRepository organizationRepository;

  public Optional<Organization> handle(UUID id) {
    return organizationRepository.findById(id);
  }
}
