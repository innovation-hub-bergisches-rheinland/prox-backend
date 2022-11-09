package de.innovationhub.prox.modules.profile.application.organization.web.dto;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.profile.domain.organization.Membership;
import de.innovationhub.prox.modules.profile.domain.organization.Organization;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrganizationDtoAssembler {
  private final TagCollectionFacade tagCollectionFacade;
  private final StorageProvider storageProvider;
  private final OrganizationDtoMapper organizationDtoMapper;

  public ReadOrganizationDto toDto(Organization organization) {
    String logoUrl = null;

    if(organization.getLogoKey() != null) {
      logoUrl = storageProvider.buildUrl(organization.getLogoKey());
    }

    if (organization.getTags() != null) {
      var tagCollectionView = tagCollectionFacade.get(organization.getTags().getTagCollectionId());
      if (tagCollectionView.isPresent()) {
        return organizationDtoMapper.toDto(organization, tagCollectionView.get().tags(), logoUrl);
      }
    }

    return organizationDtoMapper.toDto(organization, Collections.emptyList(), logoUrl);
  }

  public ReadOrganizationMembershipDto toDto(Membership membership) {
    return organizationDtoMapper.toDto(membership);
  }

  public List<ReadOrganizationMembershipDto> toDto(List<Membership> memberships) {
    return organizationDtoMapper.toDto(memberships);
  }
}
