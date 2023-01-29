package de.innovationhub.prox.modules.organization.application.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.organization.domain.Membership;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class FindOrganizationMembershipsHandler {
  private final OrganizationRepository organizationRepository;

  public List<Membership> handle(UUID orgId) {
    return organizationRepository.findById(orgId)
        .map(Organization::getMembers)
        .orElseThrow(OrganizationNotFoundException::new);
  }
}
