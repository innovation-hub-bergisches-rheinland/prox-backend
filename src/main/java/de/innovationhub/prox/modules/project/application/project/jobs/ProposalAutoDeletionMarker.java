package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.application.config.ProjectConfig;
import de.innovationhub.prox.modules.project.application.project.usecase.MarkProposalsAsStaleHandler;
import java.time.Duration;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
public class ProposalAutoDeletionMarker {

  private final MarkProposalsAsStaleHandler handler;
  private final Duration markForDeletionAfter;

  public ProposalAutoDeletionMarker(
      MarkProposalsAsStaleHandler handler, ProjectConfig projectConfig) {
    this.handler = handler;
    this.markForDeletionAfter = projectConfig.jobs().autoMarkForDelete().after();
  }

  @PostConstruct
  void log() {
    log.info(
        "Proposal Auto Deletion Marking is enabled with a duration of {} for marking",
        markForDeletionAfter);
  }

  @Scheduled(cron = "${project.jobs.auto-mark-for-delete.cron:0 0 0 * * *}")
  void autoMark() {
    handler.handle(markForDeletionAfter);
  }
}
