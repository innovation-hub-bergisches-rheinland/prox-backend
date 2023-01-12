package de.innovationhub.prox.modules.project.application.project.event;

import de.innovationhub.prox.modules.auth.contract.ProxUserStarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.auth.contract.ProxUserUnstarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.UpdateInterestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class StarIntegrationEventListeners {
  private final UpdateInterestHandler updateInterest;

  @EventListener(ProxUserStarredProjectIntegrationEvent.class)
  public void onStar(ProxUserStarredProjectIntegrationEvent event) {
    updateInterest.handle(event.projectId(), event.userId(), true);
  }

  @EventListener(ProxUserUnstarredProjectIntegrationEvent.class)
  public void onStarRemove(ProxUserUnstarredProjectIntegrationEvent event) {
    updateInterest.handle(event.projectId(), event.userId(), false);
  }
}
