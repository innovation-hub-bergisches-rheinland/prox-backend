package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import java.time.Instant;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    prefix = "project.jobs.auto-delete",
    value = "enable",
    havingValue = "true",
    matchIfMissing = true)
@Slf4j
public class ProposalAutoDelete {
  private final ProjectRepository projectRepository;

  public ProposalAutoDelete(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @PostConstruct
  void log() {
    log.info("Proposal Auto Deletion is enabled");
  }

  @Scheduled(cron = "${project.jobs.auto-delete.cron:0 0 0 * * *}")
  void run() {
    var qualifyingTimestamp = Instant.now();
    var proposalsToDelete =
        this.projectRepository.findWithStatusModifiedBefore(
            ProjectState.STALE, qualifyingTimestamp);
    if (!proposalsToDelete.isEmpty()) {
      this.projectRepository.deleteAll(proposalsToDelete);
    }
  }
}
