package de.innovationhub.prox.modules.project.application.project.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindProjectsOfPartnerHandler {
  private final ProjectRepository projectRepository;

  public Page<Project> handle(UUID partnerId, Pageable pageable) {
    return projectRepository.findByPartner(partnerId, pageable);
  }
}
