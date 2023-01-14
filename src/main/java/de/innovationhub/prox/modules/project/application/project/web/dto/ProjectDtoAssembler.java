package de.innovationhub.prox.modules.project.application.project.web.dto;

import de.innovationhub.prox.modules.organization.contract.OrganizationFacade;
import de.innovationhub.prox.modules.organization.contract.OrganizationView;
import de.innovationhub.prox.modules.project.application.ProjectPermissionEvaluator;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.tag.contract.TagFacade;
import de.innovationhub.prox.modules.user.contract.lecturer.LecturerFacade;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

  public ProjectDto toDto(Project project) {
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

    var supervisors = lecturerFacade.findByIds(
        project.getSupervisors()
            .stream()
            .map(Supervisor::getLecturerId)
            .toList()
    );

    var permissions = evaluatePermissions(project); // TODO
    var interest = evaluateInterest(project);

    return projectMapper.toDto(project, supervisors, partnerOrg, tags, permissions, interest);
  }

  public Page<ProjectDto> toDto(Page<Project> projects) {
    return projects.map(this::toDto);
  }

  public ProjectPermissions evaluatePermissions(Project project) {
    var authentication = authenticationFacade.getAuthentication();
    var permissions = new ProjectPermissions(
        projectPermissionEvaluator.hasPermission(project, authentication),
        authentication == null ? false : authentication.isAuthenticated()
    );

    return permissions;
  }

  public ProjectMetrics evaluateInterest(Project project) {
    var interest = new ProjectMetrics(
        project.getInterestedUsers().size()
    );
    return interest;
  }
}
