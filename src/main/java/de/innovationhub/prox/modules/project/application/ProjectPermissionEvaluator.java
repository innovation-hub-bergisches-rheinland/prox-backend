package de.innovationhub.prox.modules.project.application;

import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectPermissionEvaluator {
  private final ProjectRepository projectRepository;

  public boolean hasPermission(Project target, Authentication authentication) {
    if(authentication == null || !authentication.isAuthenticated()) return false;

    return authentication.getName().equals(target.getAuthor().getUserId().toString());
  }

  public boolean hasPermission(UUID projectId, Authentication authentication) {
    // To prevent querying the database
    if(authentication == null || !authentication.isAuthenticated()) return false;

    var optProject = projectRepository.findById(projectId);
    // It's better to deny than throwing an exception here
    return optProject.filter(project -> hasPermission(project, authentication)).isPresent();

  }
}
