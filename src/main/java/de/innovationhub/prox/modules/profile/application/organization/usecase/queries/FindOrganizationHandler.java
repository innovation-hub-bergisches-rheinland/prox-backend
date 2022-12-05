package de.innovationhub.prox.modules.profile.application.organization.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
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
