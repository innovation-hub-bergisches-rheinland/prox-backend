package de.innovationhub.prox.modules.auth.application;

import de.innovationhub.prox.modules.auth.contract.ProxUserStarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.auth.contract.ProxUserUnstarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.auth.domain.event.ProxUserStarredProject;
import de.innovationhub.prox.modules.auth.domain.event.ProxUserUnstarredProject;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class ProxUserIntegrationEventPublisher {
  private final ApplicationEventPublisher eventPublisher;

  @EventListener(ProxUserStarredProject.class)
  public void onProxUserStarredProject(ProxUserStarredProject event) {
    eventPublisher.publishEvent(new ProxUserStarredProjectIntegrationEvent(event.userId(), event.projectId()));
  }

  @EventListener(ProxUserUnstarredProject.class)
  public void onProxUserUnstarredProject(ProxUserUnstarredProject event) {
    eventPublisher.publishEvent(new ProxUserUnstarredProjectIntegrationEvent(event.userId(), event.projectId()));
  }
}
