package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationDto;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationProfile;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class CreateOrganizationHandler {

  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  public Organization handle(CreateOrganizationDto useCase) {
    var founder = new UserAccount(authenticationFacade.currentAuthenticated());
    var org = Organization.create(useCase.name(), founder);
    var profile = new OrganizationProfile();
    if (useCase.profile() != null) {
      var ucProfile = useCase.profile();

      profile.setHeadquarter(ucProfile.headquarter());
      profile.setHomepage(ucProfile.homepage());
      profile.setQuarters(ucProfile.quarters());
      profile.setVita(ucProfile.vita());
      profile.setFoundingDate(ucProfile.foundingDate());
      profile.setContactEmail(ucProfile.contactEmail());
      profile.setNumberOfEmployees(ucProfile.numberOfEmployees());
      profile.setSocialMediaHandles(ucProfile.socialMediaHandles());
    }
    org.setProfile(profile);

    return organizationRepository.save(org);
  }
}
