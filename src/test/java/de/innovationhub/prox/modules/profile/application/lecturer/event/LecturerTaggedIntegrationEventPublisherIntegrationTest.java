package de.innovationhub.prox.modules.profile.application.lecturer.event;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.profile.contract.LecturerTaggedIntegrationEvent;
import de.innovationhub.prox.modules.tag.application.tag.event.TagCreatedIntegrationEventPublisher;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileTagged;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
class LecturerTaggedIntegrationEventPublisherIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagCreatedIntegrationEventPublisher tagCreatedIntegrationEventPublisher;

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  ApplicationEventPublisher eventPublisher;


  @Test
  void shouldPublishEvent() {
    var tags = Set.of(UUID.randomUUID());
    var event = new LecturerProfileTagged(UUID.randomUUID(), UUID.randomUUID(), tags);
    eventPublisher.publishEvent(event);

    var events = applicationEvents.stream(LecturerTaggedIntegrationEvent.class).toList();
    assertThat(events).hasSize(1).first()
        .extracting(LecturerTaggedIntegrationEvent::lecturerId, LecturerTaggedIntegrationEvent::tags)
        .containsExactly(event.lecturerProfileId(), event.tags());
  }
}