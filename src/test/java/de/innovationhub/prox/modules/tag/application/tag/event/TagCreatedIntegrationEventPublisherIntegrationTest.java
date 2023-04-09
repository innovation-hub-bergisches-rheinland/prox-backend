package de.innovationhub.prox.modules.tag.application.tag.event;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.tag.contract.event.TagCreatedIntegrationEvent;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
class TagCreatedIntegrationEventPublisherIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagCreatedIntegrationEventPublisher tagCreatedIntegrationEventPublisher;

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  ApplicationEventPublisher eventPublisher;


  @Test
  void shouldPublishEvent() {
    var tagCreated = new TagCreated(UUID.randomUUID(), "test");
    eventPublisher.publishEvent(tagCreated);

    var events = applicationEvents.stream(TagCreatedIntegrationEvent.class).toList();
    assertThat(events).hasSize(1).first()
        .extracting(TagCreatedIntegrationEvent::id, TagCreatedIntegrationEvent::tagName)
        .containsExactly(tagCreated.id(), tagCreated.tagName());
  }
}