package de.innovationhub.prox.modules.user.application.profile.event;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.contract.profile.UserProfileTaggedIntegrationEvent;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileTagged;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@ApplicationComponent
@RequiredArgsConstructor
public class UserProfileTaggedIntegrationEventPublisher {
  private final ApplicationEventPublisher eventPublisher;

  @EventListener
  public void handle(UserProfileTagged domainEvent) {
    var integrationEvent = new UserProfileTaggedIntegrationEvent(
        domainEvent.userProfileId(),
        domainEvent.tags()
    );

    this.eventPublisher.publishEvent(integrationEvent);
  }
}
