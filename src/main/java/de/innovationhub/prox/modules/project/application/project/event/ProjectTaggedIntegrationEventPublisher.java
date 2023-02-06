package de.innovationhub.prox.modules.project.application.project.event;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.project.contract.ProjectTaggedIntegrationEvent;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class ProjectTaggedIntegrationEventPublisher {
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void handle(ProjectTagged domainEvent) {
    var integrationEvent = new ProjectTaggedIntegrationEvent(
        domainEvent.projectId(),
        domainEvent.tags()
    );

    this.eventPublisher.publishEvent(integrationEvent);
  }
}