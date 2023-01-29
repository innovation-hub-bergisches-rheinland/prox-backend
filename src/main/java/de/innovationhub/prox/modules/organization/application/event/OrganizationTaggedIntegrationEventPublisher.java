package de.innovationhub.prox.modules.organization.application.event;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.organization.contract.OrganizationTaggedIntegrationEvent;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationTagged;
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
