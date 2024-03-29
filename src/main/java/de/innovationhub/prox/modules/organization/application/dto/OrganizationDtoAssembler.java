package de.innovationhub.prox.modules.organization.application.dto;

import de.innovationhub.prox.infra.aws.StorageProvider;
import de.innovationhub.prox.modules.organization.application.OrganizationPermissionEvaluator;
import de.innovationhub.prox.modules.organization.contract.dto.MembershipDto;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationDto;
import de.innovationhub.prox.modules.organization.contract.dto.OrganizationPermissions;
import de.innovationhub.prox.modules.organization.domain.Membership;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.tag.contract.dto.TagDto;
import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileFacade;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrganizationDtoAssembler {
  private final TagCollectionFacade tagCollectionFacade;
  private final StorageProvider storageProvider;
  private final OrganizationDtoMapper organizationDtoMapper;
  private final UserProfileFacade userFacade;

  // TODO: EXPERIMENTAL
  private final OrganizationPermissionEvaluator organizationPermissionEvaluator;
  private final AuthenticationFacade authenticationFacade;

  public OrganizationDto toDto(Organization organization) {
    String logoUrl = null;

    if(organization.getLogoKey() != null) {
      logoUrl = storageProvider.buildUrl(organization.getLogoKey());
    }

    List<TagDto> tags = tagCollectionFacade.getTagCollection(organization.getTagCollectionId())
        .map(TagCollectionDto::tags)
        .orElse(Collections.emptyList());

    var permissions = new OrganizationPermissions(
        organizationPermissionEvaluator.hasPermission(organization, authenticationFacade.getAuthentication())
    );

    return organizationDtoMapper.toDto(organization, tags, logoUrl, permissions);
  }

  public MembershipDto toDto(Membership membership) {
    String name = null;
    try {
      var userView = userFacade.getByUserId(membership.getMemberId());
      name = userView.map(UserProfileDto::displayName).orElse(null);
    }
    catch (RuntimeException e) {
      log.error("Unable to fetch user displayName for user with id {}", membership.getMemberId(),
          e);
    }
    return organizationDtoMapper.toDto(membership, name);
  }

  public List<MembershipDto> toDto(List<Membership> memberships) {
    return memberships.stream().map(this::toDto).toList();
  }
}
