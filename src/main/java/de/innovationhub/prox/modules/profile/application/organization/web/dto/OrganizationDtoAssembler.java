package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrganizationDtoAssembler {
  private final TagFacade tagFacade;
  private final StorageProvider storageProvider;
  private final OrganizationDtoMapper organizationDtoMapper;

  public ReadOrganizationDto toDto(Organization organization) {
    String logoUrl = null;

    if(organization.getLogoKey() != null) {
      logoUrl = storageProvider.buildUrl(organization.getLogoKey());
    }

    List<String> tags = Collections.emptyList();
    if (organization.getTags() != null) {
      tags = tagFacade.getTags(organization.getTags());
    }

    return organizationDtoMapper.toDto(organization, tags, logoUrl);
  }

  public ReadOrganizationMembershipDto toDto(Membership membership) {
    return organizationDtoMapper.toDto(membership);
  }

  public List<ReadOrganizationMembershipDto> toDto(List<Membership> memberships) {
    return organizationDtoMapper.toDto(memberships);
  }
}
