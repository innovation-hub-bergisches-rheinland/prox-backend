package de.innovationhub.prox.modules.profile.infrastructure.persistence;

import de.innovationhub.prox.modules.commons.infrastructure.InfrastructureComponent;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.profile.domain.organization.OrganizationPort;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.mapper.OrganizationMapper;
import de.innovationhub.prox.modules.profile.infrastructure.persistence.model.query.QOrganizationEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@InfrastructureComponent
public class EBeanOrganizationPort implements OrganizationPort {

  private static final QOrganizationEntity qOrganization = new QOrganizationEntity();
  private static final OrganizationMapper organizationMapper = OrganizationMapper.MAPPER;

  @Override
  public Organization save(Organization organization) {
    var entity = organizationMapper.toPersistence(organization);
    entity.save();
    return organizationMapper.toDomain(entity);
  }

  @Override
  public List<Organization> getAll() {
    return qOrganization.findList().stream().map(organizationMapper::toDomain).toList();
  }

  @Override
  public Optional<Organization> getById(UUID id) {
    return qOrganization.id.eq(id).findOneOrEmpty().map(organizationMapper::toDomain);
  }
}
