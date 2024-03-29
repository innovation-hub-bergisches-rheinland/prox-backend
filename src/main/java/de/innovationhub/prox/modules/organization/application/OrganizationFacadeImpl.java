package de.innovationhub.prox.modules.organization.application;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.organization.application.dto.OrganizationDtoAssembler;
import de.innovationhub.prox.modules.organization.application.usecase.queries.FindAllByIdsHandler;
import de.innovationhub.prox.modules.organization.application.usecase.queries.FindOrganizationHandler;
import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
@RequiredArgsConstructor
public class OrganizationFacadeImpl implements OrganizationFacade {
  private final OrganizationDtoAssembler organizationDtoAssembler;
  private final FindOrganizationHandler findOrganizationHandler;
  private final FindAllByIdsHandler findAllByIdsHandler;

  @Override
  @Cacheable(CacheConfig.ORGANIZATIONS)
  public Optional<OrganizationDto> get(UUID id) {
    return findOrganizationHandler.handle(id)
        .map(organizationDtoAssembler::toDto);
  }

  @Override
  public List<OrganizationDto> findAllByIds(Collection<UUID> ids) {
    return findAllByIdsHandler.handle(ids)
        .stream()
        .map(organizationDtoAssembler::toDto)
        .toList();
  }
}
