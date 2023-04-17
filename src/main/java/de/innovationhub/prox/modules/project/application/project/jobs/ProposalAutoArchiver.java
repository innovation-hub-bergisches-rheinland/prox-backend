package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.time.Duration;
import java.time.Instant;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    prefix = "project.jobs.auto-archive",
    value = "enable",
    havingValue = "true",
    matchIfMissing = true)
@Slf4j
public class ProposalAutoArchiver {
  private final ProjectRepository projectRepository;
  private final Duration archiveAfter;

  public ProposalAutoArchiver(
      ProjectRepository projectRepository,
      @Value("${project.jobs.auto-archive.after:P90D}")
      Duration archiveAfter
  ) {
    this.projectRepository = projectRepository;
    this.archiveAfter = archiveAfter;
  }

  @Scheduled(cron = "${project.jobs.auto-archive.cron:0 0 0 * * *}")
  @Transactional
  public void run() {
    var qualifyingTimestamp = Instant.now().minus(archiveAfter);
    var proposalsToArchive =
        this.projectRepository.findWithStatusModifiedBefore(
            ProjectState.PROPOSED, qualifyingTimestamp);
    if (!proposalsToArchive.isEmpty()) {
      proposalsToArchive.forEach(p -> p.updateState(ProjectState.ARCHIVED));
      this.projectRepository.saveAll(proposalsToArchive);
    }
  }
}
