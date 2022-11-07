package de.innovationhub.prox.modules.profile.application.organization.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.organization.dto.CreateOrganizationDto;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationProfile;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationRepository;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class CreateOrganizationHandler implements UseCaseHandler<Organization, CreateOrganizationDto> {
  private final OrganizationRepository organizationRepository;
  private final AuthenticationFacade authenticationFacade;

  @Override
  public Organization handle(CreateOrganizationDto useCase) {
    var founder = new UserAccount(authenticationFacade.currentAuthenticated());
    var org = Organization.create(useCase.name(), founder);
    org.setProfile(new OrganizationProfile());

    return organizationRepository.save(org);
  }
}
