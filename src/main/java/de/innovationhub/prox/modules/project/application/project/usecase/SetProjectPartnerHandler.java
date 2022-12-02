package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.project.application.project.exception.ProjectNotFoundException;
import de.innovationhub.prox.modules.project.domain.project.Partner;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetProjectPartnerHandler {
  private final ProjectRepository projectRepository;

  public Project handle(UUID projectId, UUID partnerId) {
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);
    var partner = new Partner(partnerId);
    project.setPartner(partner);
    projectRepository.save(project);
    return project;
  }
}
