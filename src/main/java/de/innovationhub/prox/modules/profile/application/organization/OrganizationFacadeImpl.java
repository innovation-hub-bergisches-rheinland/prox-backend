package de.innovationhub.prox.modules.profile.application.organization;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.usecase.FindOrganization;
import de.innovationhub.prox.modules.profile.application.organization.usecase.FindOrganizationHandler;
import de.innovationhub.prox.modules.profile.contract.OrganizationFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.profile.contract.OrganizationViewMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class OrganizationFacadeImpl implements OrganizationFacade {
  private final OrganizationViewMapper organizationViewMapper;
  private final FindOrganizationHandler findOrganizationHandler;

  @Override
  public OrganizationView get(UUID id) {
    return organizationViewMapper.toView(findOrganizationHandler.handle(new FindOrganization(id)));
  }
}
