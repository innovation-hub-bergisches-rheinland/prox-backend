package de.innovationhub.prox.modules.star.application;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.application.project.event.StarIntegrationEventListeners;
import de.innovationhub.prox.modules.star.contract.event.ProjectStarredIntegrationEvent;
import de.innovationhub.prox.modules.star.contract.event.ProjectUnstarredIntegrationEvent;
import de.innovationhub.prox.modules.star.domain.StarCollection;
import de.innovationhub.prox.modules.star.domain.StarCollectionRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@RecordApplicationEvents
@Transactional
class StarCollectionIntegrationEventPublisherTest extends AbstractIntegrationTest {

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  StarCollectionRepository starCollectionRepository;

  // TODO: This is a workaround as event listeners are invoked synchronous and it will fail because
  //  project is not existent.
  //  Either create the project or use async events using rabbitmq
  @MockBean
  StarIntegrationEventListeners starIntegrationEventListeners;

  @Test
  void shouldPublishStarIntegrationEvent() {
    var userId = UUID.randomUUID();
    var projectId = UUID.randomUUID();
    var collection = new StarCollection(userId);

    collection.starProject(projectId);
    starCollectionRepository.save(collection);

    assertThat(applicationEvents.stream(ProjectStarredIntegrationEvent.class))
        .hasSize(1)
        .first()
        .satisfies(event -> {
          assertThat(event.projectId()).isEqualTo(projectId);
          assertThat(event.userId()).isEqualTo(userId);
        });
  }

  @Test
  void shouldPublishUnstarIntegrationEvent() {
    var userId = UUID.randomUUID();
    var projectId = UUID.randomUUID();
    var collection = new StarCollection(userId);
    collection.starProject(projectId);

    collection.unstarProject(projectId);
    starCollectionRepository.save(collection);

    assertThat(applicationEvents.stream(ProjectUnstarredIntegrationEvent.class))
        .hasSize(1)
        .first()
        .satisfies(event -> {
          assertThat(event.projectId()).isEqualTo(projectId);
          assertThat(event.userId()).isEqualTo(userId);
        });
  }
}