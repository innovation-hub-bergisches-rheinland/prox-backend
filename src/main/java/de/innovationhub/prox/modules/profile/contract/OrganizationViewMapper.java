package de.innovationhub.prox.modules.profile.contract;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import org.mapstruct.Mapper;

@Mapper
public interface OrganizationViewMapper {
  OrganizationView toView(Organization organization);
}
