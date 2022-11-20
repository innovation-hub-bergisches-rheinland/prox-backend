package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.auth.application.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.core.ImpossibleException;
import de.innovationhub.prox.modules.profile.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateOrganizationMembershipDto;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class UpdateOrganizationMemberHandler {
  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  public Membership handle(UUID organizationId, UUID memberId, UpdateOrganizationMembershipDto dto) {
    var authenticatedUser = authenticationFacade.currentAuthenticated();

    var org = organizationRepository.findById(organizationId)
        .orElseThrow(OrganizationNotFoundException::new);

    if (!org.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    org.updateMembership(new UserAccount(memberId), dto.role());
    organizationRepository.save(org);
    var optMembership = org.getMembers().stream()
        .filter(it -> it.getMember().getUser().getUserId().equals(memberId))
        .findFirst();

    if(optMembership.isEmpty()) {
      throw new ImpossibleException("The membership could not be applied");
    }

    return optMembership.get();
  }
}
