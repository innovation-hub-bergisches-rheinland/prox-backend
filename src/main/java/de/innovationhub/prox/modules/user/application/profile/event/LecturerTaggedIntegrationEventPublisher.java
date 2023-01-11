package de.innovationhub.prox.modules.user.application.profile.event;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.contract.LecturerTaggedIntegrationEvent;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileTagged;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class LecturerTaggedIntegrationEventPublisher {
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void handle(LecturerProfileTagged domainEvent) {
    var integrationEvent = new LecturerTaggedIntegrationEvent(
        domainEvent.lecturerProfileId(),
        domainEvent.tags()
    );

    this.eventPublisher.publishEvent(integrationEvent);
  }
}
