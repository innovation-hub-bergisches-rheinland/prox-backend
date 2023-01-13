package de.innovationhub.prox.modules.profile.application.organization.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.web.dto.CreateOrganizationRequestDto;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationProfile;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.user.contract.AuthenticationFacade;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class CreateOrganizationHandler {

  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  public Organization handle(CreateOrganizationRequestDto useCase) {
    var founder = authenticationFacade.currentAuthenticatedId();
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
