package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import jakarta.transaction.Transactional;
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
  @Transactional
  public void run() {
    var qualifyingTimestamp = Instant.now();
    var proposalsToDelete =
        this.projectRepository.findWithStatusModifiedBefore(
            ProjectState.STALE, qualifyingTimestamp);
    if (!proposalsToDelete.isEmpty()) {
      this.projectRepository.deleteAll(proposalsToDelete);
    }
  }
}
