package de.innovationhub.prox.modules.organization.application.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.core.ImpossibleException;
import de.innovationhub.prox.modules.organization.application.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.organization.application.web.dto.AddMembershipRequestDto;
import de.innovationhub.prox.modules.organization.domain.Membership;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.user.application.account.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.user.contract.account.AuthenticationFacade;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@RequiredArgsConstructor
@ApplicationComponent
public class AddOrganizationMemberHandler {
  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#organizationId, authentication)")
  public Membership handle(UUID organizationId, AddMembershipRequestDto dto) {
    var authenticatedUser = authenticationFacade.currentAuthenticatedId();

    var org = organizationRepository.findById(organizationId).orElseThrow(
        OrganizationNotFoundException::new);

    if (!org.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    org.addMember(dto.member(), dto.role());
    organizationRepository.save(org);
    var optMembership = org.getMembers().stream()
        .filter(it -> it.getMemberId().equals(dto.member()))
        .findFirst();
    if(optMembership.isEmpty()) {
      throw new ImpossibleException("The membership could not be applied. This shouldn't happen.");
    }
    return optMembership.get();
  }
}
