package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindOrganizationHandler implements UseCaseHandler<Organization, FindOrganization> {
  private final OrganizationRepository organizationRepository;

  @Override
  public Organization handle(FindOrganization useCase) {
    return organizationRepository.findById(useCase.id())
        .orElseThrow();
  }
}
