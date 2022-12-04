package de.innovationhub.prox.modules.profile.application.organization;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.organization.usecase.FindOrganizationHandler;
import de.innovationhub.prox.modules.profile.contract.OrganizationFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.profile.contract.OrganizationViewMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class OrganizationFacadeImpl implements OrganizationFacade {
  private final OrganizationViewMapper organizationViewMapper;
  private final FindOrganizationHandler findOrganizationHandler;

  @Override
  @Cacheable(CacheConfig.ORGANIZATIONS)
  public Optional<OrganizationView> get(UUID id) {
    return findOrganizationHandler.handle(id)
        .map(organizationViewMapper::toView);
  }
}
