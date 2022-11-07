package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
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
        .orElseThrow(); // TODO: Proper exception
  }
}
