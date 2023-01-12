package de.innovationhub.prox.modules.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.auth.contract.ProxUserStarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.auth.contract.ProxUserUnstarredProjectIntegrationEvent;
import de.innovationhub.prox.modules.auth.domain.ProxUser;
import de.innovationhub.prox.modules.auth.domain.ProxUserRepository;
import de.innovationhub.prox.modules.project.application.project.event.StarIntegrationEventListeners;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@RecordApplicationEvents
@Transactional
class ProxUserIntegrationEventPublisherTest extends AbstractIntegrationTest {

  @Autowired
  ApplicationEvents applicationEvents;

  @Autowired
  ProxUserRepository userRepository;

  // TODO: This is a workaround as event listeners are invoked synchronous and it will fail because
  //  project is not existent.
  //  Either create the project or use async events using rabbitmq
  @MockBean
  StarIntegrationEventListeners starIntegrationEventListeners;

  @Test
  void shouldPublishStarIntegrationEvent() {
    var user = createTestUser();
    var projectId = UUID.randomUUID();

    user.starProject(projectId);

    userRepository.save(user);

    assertThat(applicationEvents.stream(ProxUserStarredProjectIntegrationEvent.class))
        .hasSize(1)
        .first()
        .satisfies(event -> {
          assertThat(event.projectId()).isEqualTo(projectId);
          assertThat(event.userId()).isEqualTo(user.getId());
        });
  }

  @Test
  void shouldPublishUnstarIntegrationEvent() {
    var user = createTestUser();
    var projectId = UUID.randomUUID();
    user.starProject(projectId);

    user.unstarProject(projectId);

    userRepository.save(user);

    assertThat(applicationEvents.stream(ProxUserUnstarredProjectIntegrationEvent.class))
        .hasSize(1)
        .first()
        .satisfies(event -> {
          assertThat(event.projectId()).isEqualTo(projectId);
          assertThat(event.userId()).isEqualTo(user.getId());
        });
  }

  private ProxUser createTestUser() {
    return ProxUser.register(UUID.randomUUID());
  }
}