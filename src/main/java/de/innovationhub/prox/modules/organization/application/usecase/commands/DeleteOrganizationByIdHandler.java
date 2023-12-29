package de.innovationhub.prox.modules.organization.application.usecase.commands;

import de.innovationhub.prox.commons.exception.UnauthorizedAccessException;
import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.UUID;

@RequiredArgsConstructor
@ApplicationComponent
public class DeleteOrganizationByIdHandler {

  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#organizationId, authentication)")
  public boolean handle(UUID organizationId){
    var currentAuthenticatedUserId = authenticationFacade.currentAuthenticatedId();

    var org = organizationRepository.findById(organizationId)
        .orElseThrow(OrganizationNotFoundException::new);

    if (!org.isInRole(currentAuthenticatedUserId, OrganizationRole.ADMIN)){
      throw new UnauthorizedAccessException();
    }
    org.deleteOrg();
    organizationRepository.deleteById(organizationId);
    return true;
  }
}
