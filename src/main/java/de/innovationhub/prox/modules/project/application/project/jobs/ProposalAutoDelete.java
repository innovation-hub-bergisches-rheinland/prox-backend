package de.innovationhub.prox.modules.project.application.project.jobs;


import de.innovationhub.prox.modules.project.application.project.usecase.DeleteStaleProposalsHandler;
import java.time.Duration;
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
  private final DeleteStaleProposalsHandler handler;

  public ProposalAutoDelete(DeleteStaleProposalsHandler handler) {
    this.handler = handler;
  }

  @PostConstruct
  void log() {
    log.info("Proposal Auto Deletion is enabled");
  }

  @Scheduled(cron = "${project.jobs.auto-delete.cron:0 0 0 * * *}")
  void autoDelete() {
    // Delete immediately
    handler.handle(Duration.ZERO);
  }
}
