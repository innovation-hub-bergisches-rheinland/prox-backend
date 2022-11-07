package de.innovationhub.prox.modules.project.application.project.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationComponent
public class DeleteStaleProposalsHandler {
  private final ProjectRepository projectRepository;

  public void handle(Duration deleteAfter) {
    var qualifyingTimestamp = Instant.now().minus(deleteAfter);
    var proposalsToDelete =
        this.projectRepository.findWithStatusModifiedBefore(
            ProjectState.STALE, qualifyingTimestamp);
    if (!proposalsToDelete.isEmpty()) {
      this.projectRepository.deleteAll(proposalsToDelete);
    }
  }
}
