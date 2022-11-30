package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.auth.application.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.UpdateOrganizationDto;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationProfile;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@RequiredArgsConstructor
@ApplicationComponent
public class UpdateOrganizationHandler {
  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  @PreAuthorize("@organizationPermissionEvaluator.hasPermission(#id, authentication)")
  public Organization handle(UUID id, UpdateOrganizationDto useCase) {
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
