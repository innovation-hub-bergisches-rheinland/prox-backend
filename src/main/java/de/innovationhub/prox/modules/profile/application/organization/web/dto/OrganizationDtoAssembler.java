package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.profile.application.organization.OrganizationPermissionEvaluator;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.user.contract.KeycloakUserFacade;
import de.innovationhub.prox.modules.user.contract.KeycloakUserView;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrganizationDtoAssembler {
  private final TagFacade tagFacade;
  private final StorageProvider storageProvider;
  private final OrganizationDtoMapper organizationDtoMapper;
  private final KeycloakUserFacade keycloakUserFacade;

  // TODO: EXPERIMENTAL
  private final OrganizationPermissionEvaluator organizationPermissionEvaluator;
  private final AuthenticationFacade authenticationFacade;

  public OrganizationDto toDto(Organization organization) {
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

  public MembershipDto toDto(Membership membership) {
    String name = null;
    try {
      var userView = keycloakUserFacade.findById(membership.getMemberId());
      name = userView.map(KeycloakUserView::name).orElse(null);
    }
    catch (RuntimeException e) {
      log.error("Unable to fetch user name for user with id {}", membership.getMemberId(), e);
    }
    return organizationDtoMapper.toDto(membership, name);
  }

  public List<MembershipDto> toDto(List<Membership> memberships) {
    return memberships.stream().map(this::toDto).toList();
  }
}
