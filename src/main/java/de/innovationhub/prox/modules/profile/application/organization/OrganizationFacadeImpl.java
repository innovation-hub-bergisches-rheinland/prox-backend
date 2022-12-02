package de.innovationhub.prox.modules.profile.application.organization;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.profile.application.organization.usecase.FindOrganizationHandler;
import de.innovationhub.prox.modules.profile.contract.OrganizationFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.profile.contract.OrganizationViewMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class OrganizationFacadeImpl implements OrganizationFacade {

  private final OrganizationViewMapper organizationViewMapper;
  private final FindOrganizationHandler findOrganizationHandler;

  @Override
  public Optional<OrganizationView> get(UUID id) {
    return findOrganizationHandler.handle(id)
        .map(organizationViewMapper::toView);
  }

  @Override
  public boolean isMember(UUID organizationId, UUID userId) {
    return findOrganizationHandler.handle(organizationId)
        .map(organization -> organization.getMembers().stream()
            .anyMatch(member -> member.getMemberId().equals(userId)))
        .orElseThrow(OrganizationNotFoundException::new);
  }
}
