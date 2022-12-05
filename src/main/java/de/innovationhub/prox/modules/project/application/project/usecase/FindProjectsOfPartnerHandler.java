package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindProjectsOfPartnerHandler {
  private final ProjectRepository projectRepository;

  public List<Project> handle(UUID partnerId) {
    return projectRepository.findByPartner(partnerId);
  }
}