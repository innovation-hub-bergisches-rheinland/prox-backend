package de.innovationhub.prox.infrastructure.persistence.mapper;

import de.innovationhub.prox.infrastructure.persistence.model.OrganizationEntity;
import de.innovationhub.prox.infrastructure.persistence.model.OrganizationMembershipEntity;
import de.innovationhub.prox.infrastructure.persistence.model.OrganizationMembershipPK;
import de.innovationhub.prox.profile.organization.Membership;
import de.innovationhub.prox.profile.organization.Organization;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrganizationMapper {

  OrganizationMapper MAPPER = Mappers.getMapper(OrganizationMapper.class);

  @Mapping(target = "id", source = "id")
  @Mapping(target = ".", source = "profile")
  @Mapping(target = "memberships", source = ".")
  @Mapping(target = "socialMedia", source = "profile.socialMediaHandles")
  OrganizationEntity toPersistence(Organization organization);

  default List<OrganizationMembershipEntity> mapMemberships(Organization organization) {
    var members = organization.getMembers();
    if (members == null) {
      return null;
    }

    var list = new ArrayList<OrganizationMembershipEntity>();

    for (var entry : members.entrySet()) {
      var pk = new OrganizationMembershipPK(organization.getId(), entry.getKey());
      var membershipEntity = new OrganizationMembershipEntity();
      membershipEntity.setId(pk);
      membershipEntity.setRole(entry.getValue().getRole());

      list.add(membershipEntity);
    }

    return list;
  }

  default Map<UUID, Membership> map(List<OrganizationMembershipEntity> value) {
    if (value == null) {
      return Collections.emptyMap();
    }
    return value.stream()
      .collect(
        Collectors.toMap(OrganizationMembershipEntity::getUserId, e -> new Membership(e.getRole())));
  }

  @Mapping(target = "id", source = "id")
  @Mapping(target = "profile", source = ".")
  @Mapping(target = "profile.socialMediaHandles", source = "socialMedia")
  @Mapping(target = "members", source = "memberships")
  Organization toDomain(OrganizationEntity entity);
}
