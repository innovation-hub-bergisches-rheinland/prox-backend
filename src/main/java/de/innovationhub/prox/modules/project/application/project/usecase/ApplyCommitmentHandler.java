package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class ApplyCommitmentHandler {
  private final ProjectRepository projectRepository;
  private final AuthenticationFacade authenticationFacade;

  @PreAuthorize("hasRole('professor')")
  public Project handle(UUID projectId) {
    var auth = authenticationFacade.currentAuthenticated();
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    project.offer(new Supervisor(auth));

    return projectRepository.save(project);
  }
}
