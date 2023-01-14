package de.innovationhub.prox.modules.organization.application.web.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.organization.application.OrganizationPermissionEvaluator;
import de.innovationhub.prox.modules.organization.domain.Membership;
import de.innovationhub.prox.modules.organization.domain.Organization;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.contract.account.AuthenticationFacade;
import de.innovationhub.prox.modules.user.contract.account.ProxUserView;
import de.innovationhub.prox.modules.user.contract.account.UserFacade;
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
  private final UserFacade userFacade;

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
      var userView = userFacade.findById(membership.getMemberId());
      name = userView.map(ProxUserView::name).orElse(null);
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
