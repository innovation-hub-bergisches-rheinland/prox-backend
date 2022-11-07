package de.innovationhub.prox.modules.profile.application.organization.dto;

import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
interface OrganizationDtoMapper {

  OrganizationDtoMapper INSTANCE = Mappers.getMapper(OrganizationDtoMapper.class);

  @Mapping(target = "tags", source = "tags")
  ReadOrganizationDto toDto(Organization organization, List<String> tags);
}
