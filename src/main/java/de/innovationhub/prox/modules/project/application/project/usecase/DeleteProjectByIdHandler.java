package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class DeleteProjectByIdHandler {
  private final ProjectRepository projectRepository;
  private final AuthenticationFacade authenticationFacade;

  public void handle(UUID id) {
    var auth = authenticationFacade.currentAuthenticated();
    var project = projectRepository.findById(id)
        .orElseThrow(ProjectNotFoundException::new);

    // TODO: Authorization check
    projectRepository.delete(project);
  }
}
