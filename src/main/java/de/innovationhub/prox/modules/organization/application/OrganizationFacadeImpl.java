package de.innovationhub.prox.modules.organization.application;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.usecase.queries.FindOrganizationHandler;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.organization.contract.OrganizationView;
import de.innovationhub.prox.modules.organization.contract.OrganizationViewMapper;
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
