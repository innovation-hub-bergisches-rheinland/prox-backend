package de.innovationhub.prox.modules.organization.application.event;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationTagged;
import de.innovationhub.prox.modules.profile.contract.OrganizationTaggedIntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class OrganizationTaggedIntegrationEventPublisher {
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void handle(OrganizationTagged domainEvent) {
    var integrationEvent = new OrganizationTaggedIntegrationEvent(
        domainEvent.organizationId(),
        domainEvent.tags()
    );

    this.eventPublisher.publishEvent(integrationEvent);
  }
}
