package de.innovationhub.prox.modules.organization.application.dto;

import de.innovationhub.prox.modules.organization.contract.dto.MembershipDto;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationPermissions;
import de.innovationhub.prox.modules.organization.domain.Membership;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
interface OrganizationDtoMapper {

  OrganizationDtoMapper INSTANCE = Mappers.getMapper(OrganizationDtoMapper.class);

  @Mapping(target = "tags", source = "tags")
  @Mapping(target = "logoUrl", source = "logoUrl")
  OrganizationDto toDto(Organization organization, List<TagDto> tags, String logoUrl, OrganizationPermissions permissions);

  @Mapping(target = "member", source = "membership.memberId")
  @Mapping(target = "name", source = "name")
  MembershipDto toDto(Membership membership, String name);
}
