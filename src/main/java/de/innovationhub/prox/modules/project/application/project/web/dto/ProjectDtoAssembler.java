package de.innovationhub.prox.modules.project.application.project.web.dto;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.profile.contract.LecturerFacade;
import de.innovationhub.prox.modules.profile.contract.LecturerView;
import de.innovationhub.prox.modules.profile.contract.OrganizationFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationView;
import de.innovationhub.prox.modules.project.application.ProjectPermissionEvaluator;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProjectDtoAssembler {

  private final TagFacade tagFacade;
  private final OrganizationFacade organizationFacade;
  private final LecturerFacade lecturerFacade;
  private final ProjectMapper projectMapper;

  // TODO: EXPERIMENTAL
  private final ProjectPermissionEvaluator projectPermissionEvaluator;
  private final AuthenticationFacade authenticationFacade;

  public ReadProjectDto toDto(Project project) {
    OrganizationView partnerOrg = null;

    if (project.getPartner() != null) {
      var orgId = project.getPartner().getOrganizationId();
      partnerOrg = organizationFacade.get(orgId)
          .orElse(new OrganizationView(orgId, null));
    }

    List<String> tags = Collections.emptyList();
    if (project.getTags() != null) {
      tags = tagFacade.getTags(project.getTags());
    }

    var supervisors = project.getSupervisors()
        .stream()
        .map(s -> lecturerFacade.get(s.getLecturerId()).orElse(new LecturerView(s.getLecturerId(), null)))
        .toList();

    var permissions = new ProjectPermissions(projectPermissionEvaluator.hasPermission(project, authenticationFacade.getAuthentication()));

    return projectMapper.toDto(project, supervisors, partnerOrg, tags, permissions);
  }

  public ReadProjectListDto toDto(List<Project> projects) {
    return new ReadProjectListDto(projects.stream()
        .map(this::toDto)
        .toList());
  }
}
