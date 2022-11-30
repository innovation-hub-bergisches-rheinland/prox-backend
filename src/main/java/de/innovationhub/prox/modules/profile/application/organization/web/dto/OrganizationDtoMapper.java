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
  ReadOrganizationDto toDto(Organization organization, List<String> tags, String logoUrl);

  @Mapping(target = "member", source = "memberId")
  ReadOrganizationMembershipDto toDto(Membership membership);

  List<ReadOrganizationMembershipDto> toDto(List<Membership> memberships);
}
