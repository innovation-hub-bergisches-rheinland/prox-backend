package de.innovationhub.prox.modules.profile.application.organization.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import de.innovationhub.prox.modules.user.application.user.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SetOrganizationTagsHandler {

  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#organizationId, authentication)")
  public List<UUID> handle(UUID organizationId,
      List<UUID> tags) {
    var organization = organizationRepository.findById(organizationId)
        .orElseThrow(OrganizationNotFoundException::new);
    var authenticatedUser = authenticationFacade.currentAuthenticatedId();

    if (!organization.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    organization.setTags(tags);
    organizationRepository.save(organization);
    return List.copyOf(organization.getTags());
  }
}
