package de.innovationhub.prox.modules.project.application.project.event;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.application.project.usecase.commands.UpdateInterestHandler;
import de.innovationhub.prox.modules.star.contract.ProjectStarredIntegrationEvent;
import de.innovationhub.prox.modules.star.contract.ProjectUnstarredIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class StarIntegrationEventListeners {

  private final UpdateInterestHandler updateInterest;

  @EventListener(ProjectStarredIntegrationEvent.class)
  public void onStar(ProjectStarredIntegrationEvent event) {
    updateInterest.handle(event.projectId(), event.userId(), true);
  }

  @EventListener(ProjectUnstarredIntegrationEvent.class)
  public void onStarRemove(ProjectUnstarredIntegrationEvent event) {
    updateInterest.handle(event.projectId(), event.userId(), false);
  }
}