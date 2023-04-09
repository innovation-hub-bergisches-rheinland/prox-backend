package de.innovationhub.prox.modules.organization.application;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.application.usecase.queries.FindAllByIdsHandler;
import de.innovationhub.prox.modules.organization.application.usecase.queries.FindAllWithAnyTagsHandler;
import de.innovationhub.prox.modules.organization.application.usecase.queries.FindOrganizationHandler;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.organization.contract.OrganizationView;
import de.innovationhub.prox.modules.organization.contract.OrganizationViewMapper;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class OrganizationFacadeImpl implements OrganizationFacade {
  private final OrganizationViewMapper organizationViewMapper;
  private final FindOrganizationHandler findOrganizationHandler;
  private final FindAllWithAnyTagsHandler findAllWithAnyTagsHandler;
  private final FindAllByIdsHandler findAllByIdsHandler;

  @Override
  @Cacheable(CacheConfig.ORGANIZATIONS)
  public Optional<OrganizationView> get(UUID id) {
    return findOrganizationHandler.handle(id)
        .map(organizationViewMapper::toView);
  }

  @Override
  public List<OrganizationView> findAllWithAnyTags(Collection<UUID> tags) {
    return findAllWithAnyTagsHandler.handle(tags)
        .stream()
        .map(organizationViewMapper::toView)
        .toList();
  }

  @Override
  public List<OrganizationView> findAllByIds(Collection<UUID> ids) {
    return findAllByIdsHandler.handle(ids)
        .stream()
        .map(organizationViewMapper::toView)
        .toList();
  }
}
