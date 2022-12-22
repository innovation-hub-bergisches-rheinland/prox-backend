package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    prefix = "project.jobs.auto-mark-for-delete",
    value = "enable",
    havingValue = "true",
    matchIfMissing = true)
@Slf4j
public class ProposalAutoStaleMarker {

  private final ProjectRepository projectRepository;
  private final Duration markForDeletionAfter;

  public ProposalAutoStaleMarker(
      ProjectRepository projectRepository,
      @Value("${project.jobs.auto-mark-for-delete:P90D}")
      Duration markForDeletionAfter
  ) {
    this.projectRepository = projectRepository;
    this.markForDeletionAfter = markForDeletionAfter;
  }

  @Scheduled(cron = "${project.jobs.auto-mark-for-delete.cron:0 0 0 * * *}")
  void run() {
    var qualifyingTimestamp = Instant.now().minus(markForDeletionAfter);
    var proposalsToMark =
        this.projectRepository.findWithStatusModifiedBefore(
            ProjectState.ARCHIVED, qualifyingTimestamp);
    if (!proposalsToMark.isEmpty()) {
      proposalsToMark.forEach(p -> p.updateState(ProjectState.STALE));
      this.projectRepository.saveAll(proposalsToMark);
    }
  }
}
