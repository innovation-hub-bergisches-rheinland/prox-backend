package de.innovationhub.prox.infrastructure.persistence.port;

import de.innovationhub.prox.commons.InfrastructureComponent;
import de.innovationhub.prox.infrastructure.persistence.mapper.OrganizationMapper;
import de.innovationhub.prox.infrastructure.persistence.model.query.QOrganizationEntity;
import de.innovationhub.prox.profile.organization.Organization;
import de.innovationhub.prox.profile.organization.OrganizationPort;
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
    return qOrganization
      .findList()
      .stream()
      .map(organizationMapper::toDomain)
      .toList();
  }

  @Override
  public Optional<Organization> getById(UUID id) {
    return qOrganization
      .id.eq(id)
      .findOneOrEmpty()
      .map(organizationMapper::toDomain);
  }
}
