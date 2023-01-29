package de.innovationhub.prox.modules.tag.application.tag.event;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.tag.contract.TagCreatedIntegrationEvent;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class TagCreatedIntegrationEventPublisher {
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void handle(TagCreated domainEvent) {
    var integrationEvent = new TagCreatedIntegrationEvent(
        domainEvent.id(),
        domainEvent.tagName()
    );

    this.eventPublisher.publishEvent(integrationEvent);
  }
}
