package de.innovationhub.prox.modules.project.application.project.dto;

import de.innovationhub.prox.modules.profile.contract.LecturerFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.TagCollectionView;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProjectDtoAssembler {
  private final TagCollectionFacade tagCollectionFacade;
  private final OrganizationFacade organizationFacade;
  private final LecturerFacade lecturerFacade;
  private final ProjectMapper projectMapper;

  public ReadProjectDto toDto(Project project) {
    OrganizationView partnerOrg = null;
    TagCollectionView tagCollectionView = null;

    if(project.getPartner() != null) {
      var optOrg = organizationFacade.get(project.getPartner().getOrganizationId());
      if(optOrg.isPresent()) {
        partnerOrg = optOrg.get();
      }
    }
    if(project.getTags() != null) {
      var optTags = tagCollectionFacade.get(project.getTags().getTagCollectionId());
      if(optTags.isPresent()) {
        tagCollectionView = optTags.get();
      }
    }
    var supervisors = project.getSupervisors()
        .stream().map(s -> lecturerFacade.get(s.getLecturerId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
    return projectMapper.toDto(project, supervisors, partnerOrg, tagCollectionView);
  }
}
