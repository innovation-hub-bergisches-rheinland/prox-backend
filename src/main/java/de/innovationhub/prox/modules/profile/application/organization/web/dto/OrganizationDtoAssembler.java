package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.auth.contract.UserFacade;
import de.innovationhub.prox.modules.auth.contract.UserView;
import de.innovationhub.prox.modules.profile.application.organization.OrganizationPermissionEvaluator;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrganizationDtoAssembler {
  private final TagFacade tagFacade;
  private final StorageProvider storageProvider;
  private final OrganizationDtoMapper organizationDtoMapper;
  private final UserFacade userFacade;

  // TODO: EXPERIMENTAL
  private final OrganizationPermissionEvaluator organizationPermissionEvaluator;
  private final AuthenticationFacade authenticationFacade;

  public ReadOrganizationDto toDto(Organization organization) {
    String logoUrl = null;

    if(organization.getLogoKey() != null) {
      logoUrl = storageProvider.buildUrl(organization.getLogoKey());
    }

    List<String> tags = Collections.emptyList();
    if (organization.getTags() != null) {
      tags = tagFacade.getTags(organization.getTags());
    }

    var permissions = new OrganizationPermissions(
        organizationPermissionEvaluator.hasPermission(organization, authenticationFacade.getAuthentication())
    );

    return organizationDtoMapper.toDto(organization, tags, logoUrl, permissions);
  }

  public ReadOrganizationMembershipDto toDto(Membership membership) {
    String name = null;
    try {
      var userView = userFacade.findById(membership.getMemberId());
      name = userView.map(UserView::name).orElse(null);
    }
    catch (RuntimeException e) {
      log.error("Unable to fetch user name for user with id {}", membership.getMemberId(), e);
    }
    return organizationDtoMapper.toDto(membership, name);
  }

  public List<ReadOrganizationMembershipDto> toDto(List<Membership> memberships) {
    return memberships.stream().map(this::toDto).toList();
  }

  public Page<ReadOrganizationDto> toDto(Page<Organization> organizations) {
    return organizations.map(this::toDto);
  }
}
