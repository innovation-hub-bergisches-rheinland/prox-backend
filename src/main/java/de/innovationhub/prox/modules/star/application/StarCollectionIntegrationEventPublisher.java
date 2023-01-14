package de.innovationhub.prox.modules.star.application;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.star.contract.ProjectStarredIntegrationEvent;
import de.innovationhub.prox.modules.star.contract.ProjectUnstarredIntegrationEvent;
import de.innovationhub.prox.modules.star.domain.event.ProjectStarred;
import de.innovationhub.prox.modules.star.domain.event.ProjectUnstarred;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class StarCollectionIntegrationEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  @EventListener(ProjectStarred.class)
  public void onProxUserStarredProject(ProjectStarred event) {
    eventPublisher.publishEvent(
        new ProjectStarredIntegrationEvent(event.userId(), event.projectId()));
  }

  @EventListener(ProjectUnstarred.class)
  public void onProxUserUnstarredProject(ProjectUnstarred event) {
    eventPublisher.publishEvent(
        new ProjectUnstarredIntegrationEvent(event.userId(), event.projectId()));
  }
}
