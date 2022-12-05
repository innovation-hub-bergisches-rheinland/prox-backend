package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class ArchiveInactiveProposalsHandler {
  private final ProjectRepository projectRepository;

  public void handle(Duration archiveAfter) {
    var qualifyingTimestamp = Instant.now().minus(archiveAfter);
    var proposalsToArchive =
        this.projectRepository.findWithStatusModifiedBefore(
            ProjectState.PROPOSED, qualifyingTimestamp);
    if (!proposalsToArchive.isEmpty()) {
      proposalsToArchive.forEach(Project::archive);
      this.projectRepository.saveAll(proposalsToArchive);
    }
  }
}
