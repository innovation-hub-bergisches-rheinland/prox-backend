package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.auth.application.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetOrganizationTagsHandler {

  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  public List<UUID> handle(UUID organizationId,
      List<UUID> tags) {
    var organization = organizationRepository.findById(organizationId)
        .orElseThrow(OrganizationNotFoundException::new);
    var authenticatedUser = authenticationFacade.currentAuthenticated();

    if (!organization.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    organization.setTags(tags);
    organizationRepository.save(organization);
    return List.copyOf(organization.getTags());
  }
}
