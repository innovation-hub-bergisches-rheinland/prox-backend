package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.auth.application.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class RemoveOrganizationMemberHandler {
  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  public void handle(UUID organizationId, UUID memberId) {
    var authenticatedUser = authenticationFacade.currentAuthenticated();

    var org = organizationRepository.findById(organizationId).orElseThrow(
        OrganizationNotFoundException::new);

    if (!org.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    org.removeMember(new UserAccount(memberId));
    organizationRepository.save(org);
  }
}
