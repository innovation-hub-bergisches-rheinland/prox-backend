package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
interface OrganizationDtoMapper {

  OrganizationDtoMapper INSTANCE = Mappers.getMapper(OrganizationDtoMapper.class);

  @Mapping(target = "tags", source = "tags")
  @Mapping(target = "logoUrl", source = "logoUrl")
  OrganizationDto toDto(Organization organization, List<String> tags, String logoUrl, OrganizationPermissions permissions);

  @Mapping(target = "member", source = "membership.memberId")
  @Mapping(target = "name", source = "name")
  MembershipDto toDto(Membership membership, String name);
}
