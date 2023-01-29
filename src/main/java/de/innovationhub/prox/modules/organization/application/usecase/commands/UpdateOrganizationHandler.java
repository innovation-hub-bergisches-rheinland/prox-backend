package de.innovationhub.prox.modules.organization.application.usecase.commands;

import de.innovationhub.prox.commons.exception.UnauthorizedAccessException;
import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.dto.CreateOrganizationRequestDto;
import de.innovationhub.prox.modules.organization.application.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.organization.domain.OrganizationProfile;
import de.innovationhub.prox.modules.organization.domain.OrganizationRepository;
import de.innovationhub.prox.modules.organization.domain.OrganizationRole;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@RequiredArgsConstructor
@ApplicationComponent
public class UpdateOrganizationHandler {
  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#id, authentication)")
  public Organization handle(UUID id, CreateOrganizationRequestDto useCase) {
    var authenticatedUser = authenticationFacade.currentAuthenticatedId();
    var org = organizationRepository.findById(id)
        .orElseThrow(OrganizationNotFoundException::new);

    if (!org.isInRole(authenticatedUser, OrganizationRole.ADMIN)) {
      throw new UnauthorizedAccessException();
    }

    org.setName(useCase.name());
    var ucProfile = useCase.profile();
    if (ucProfile != null) {
      var profile = org.getProfile();
      if (profile == null) {
        profile = new OrganizationProfile();
      }

      profile.setHeadquarter(ucProfile.headquarter());
      profile.setHomepage(ucProfile.homepage());
      profile.setQuarters(ucProfile.quarters());
      profile.setVita(ucProfile.vita());
      profile.setFoundingDate(ucProfile.foundingDate());
      profile.setContactEmail(ucProfile.contactEmail());
      profile.setNumberOfEmployees(ucProfile.numberOfEmployees());
      profile.setSocialMediaHandles(ucProfile.socialMediaHandles());
      org.setProfile(profile);
    }

    return organizationRepository.save(org);
  }
}
