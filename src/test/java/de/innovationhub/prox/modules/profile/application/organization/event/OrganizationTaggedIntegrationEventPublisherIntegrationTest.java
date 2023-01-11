package de.innovationhub.prox.modules.profile.application.organization.event;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.organization.domain.events.OrganizationTagged;
import de.innovationhub.prox.modules.profile.contract.LecturerTaggedIntegrationEvent;
import de.innovationhub.prox.modules.profile.contract.OrganizationTaggedIntegrationEvent;
import de.innovationhub.prox.modules.tag.application.tag.event.TagCreatedIntegrationEventPublisher;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
class OrganizationTaggedIntegrationEventPublisherIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagCreatedIntegrationEventPublisher tagCreatedIntegrationEventPublisher;

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  ApplicationEventPublisher eventPublisher;


  @Test
  void shouldPublishEvent() {
    var tags = List.of(UUID.randomUUID());
    var event = new OrganizationTagged(UUID.randomUUID(), tags);
    eventPublisher.publishEvent(event);

    var events = applicationEvents.stream(OrganizationTaggedIntegrationEvent.class).toList();
    assertThat(events).hasSize(1).first()
        .extracting(OrganizationTaggedIntegrationEvent::organizationId, OrganizationTaggedIntegrationEvent::tags)
        .containsExactly(event.organizationId(), event.tags());
  }
}