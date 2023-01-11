package de.innovationhub.prox.modules.project.application.project.event;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.contract.ProjectTaggedIntegrationEvent;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import de.innovationhub.prox.modules.tag.application.tag.event.TagCreatedIntegrationEventPublisher;
import de.innovationhub.prox.modules.tag.contract.TagCreatedIntegrationEvent;
import de.innovationhub.prox.modules.tag.domain.tag.events.TagCreated;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
class ProjectTaggedIntegrationEventPublisherIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  TagCreatedIntegrationEventPublisher tagCreatedIntegrationEventPublisher;

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  ApplicationEventPublisher eventPublisher;


  @Test
  void shouldPublishEvent() {
    var tags = List.of(UUID.randomUUID());
    var event = new ProjectTagged(UUID.randomUUID(), tags);
    eventPublisher.publishEvent(event);

    var events = applicationEvents.stream(ProjectTaggedIntegrationEvent.class).toList();
    assertThat(events).hasSize(1).first()
        .extracting(ProjectTaggedIntegrationEvent::projectId, ProjectTaggedIntegrationEvent::tags)
        .containsExactly(event.projectId(), event.tags());
  }
}