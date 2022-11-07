package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.application.config.ProjectConfig;
import de.innovationhub.prox.modules.project.application.project.usecase.ArchiveInactiveProposalsHandler;
import java.time.Duration;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
  private final ArchiveInactiveProposalsHandler handler;
  private final Duration archiveAfter;

  public ProposalAutoArchiver(
      ArchiveInactiveProposalsHandler handler, ProjectConfig projectConfig) {
    this.handler = handler;
    this.archiveAfter = projectConfig.jobs().autoArchive().after();
  }

  @PostConstruct
  void log() {
    log.info(
        "Proposal Auto Archiving is enabled with a duration of {} for archiving", archiveAfter);
  }

  @Scheduled(cron = "${project.jobs.auto-archive.cron:0 0 0 * * *}")
  void autoArchive() {
    handler.handle(archiveAfter);
  }
}
