package de.innovationhub.prox.modules.organization.contract;

import de.innovationhub.prox.modules.organization.domain.Organization;
import org.mapstruct.Mapper;

@Mapper
public interface OrganizationViewMapper {
  OrganizationView toView(Organization organization);
}
