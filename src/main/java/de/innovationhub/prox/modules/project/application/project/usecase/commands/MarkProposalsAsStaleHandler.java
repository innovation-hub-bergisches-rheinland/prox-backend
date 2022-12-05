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
public class MarkProposalsAsStaleHandler {
  private final ProjectRepository projectRepository;

  public void handle(Duration markAsStaleAfter) {
    var qualifyingTimestamp = Instant.now().minus(markAsStaleAfter);
    var proposalsToMark =
        this.projectRepository.findWithStatusModifiedBefore(
            ProjectState.ARCHIVED, qualifyingTimestamp);
    if (!proposalsToMark.isEmpty()) {
      proposalsToMark.forEach(Project::stale);
      this.projectRepository.saveAll(proposalsToMark);
    }
  }
}
