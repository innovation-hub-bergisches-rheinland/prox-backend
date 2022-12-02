package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.profile.contract.OrganizationFacade;
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
  private final OrganizationFacade organizationFacade;
  private final AuthenticationFacade authenticationFacade;

  public Project handle(UUID projectId, UUID partnerId) {
    var auth = authenticationFacade.currentAuthenticatedId();
    var project = projectRepository.findById(projectId)
        .orElseThrow(ProjectNotFoundException::new);

    var isMember = this.organizationFacade.isMember(partnerId, auth);
    if (!isMember) {
      throw new IllegalArgumentException("User is not a member of the organization");
    }

    var partner = new Partner(partnerId);
    project.setPartner(partner);
    projectRepository.save(project);
    return project;
  }
}
