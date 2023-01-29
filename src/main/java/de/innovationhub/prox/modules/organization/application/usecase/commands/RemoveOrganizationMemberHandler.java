package de.innovationhub.prox.modules.organization.application.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.user.application.user.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@RequiredArgsConstructor
@ApplicationComponent
public class RemoveOrganizationMemberHandler {
  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  @Transactional
  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#organizationId, authentication)")
  public void handle(UUID organizationId, UUID memberId) {
    var authenticatedUser = authenticationFacade.currentAuthenticatedId();

    var org = organizationRepository.findById(organizationId).orElseThrow(
        OrganizationNotFoundException::new);

    if (!org.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    org.removeMember(memberId);
    organizationRepository.save(org);
  }
}
