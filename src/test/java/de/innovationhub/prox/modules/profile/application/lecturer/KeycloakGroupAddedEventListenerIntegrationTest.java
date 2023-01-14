package de.innovationhub.prox.modules.profile.application.lecturer;

import static de.innovationhub.prox.CustomAssertions.assertEventually;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.config.MessagingConfig;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.contract.account.ProxUserView;
import de.innovationhub.prox.modules.user.contract.account.UserFacade;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class KeycloakGroupAddedEventListenerIntegrationTest extends AbstractIntegrationTest {

  private static final String ROUTING_KEY_GROUP_ADD = "KK.EVENT.ADMIN.test-realm.SUCCESS.GROUP_MEMBERSHIP.CREATE";

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  LecturerRepository lecturerRepository;

  @MockBean
  UserFacade userFacade;

  @Test
  // TODO: Fix this test: Context: https://stackoverflow.com/questions/75111282/using-spring-boot-mockbean-annotation-in-rabbitmq-listener
  @Disabled(value = "This test is disabled because I don't know how to get the mock of the user facade to work")
  void shouldCreateLecturer() {
    var userId = UUID.randomUUID();
    var user = new ProxUserView(userId, "Xavier Tester", "xavier.tester@example.com");
    when(userFacade.findById(userId)).thenReturn(Optional.of(user));
    var group = "professor";
    var event = createGroupAddedEvent(userId, group);

    rabbitTemplate.convertAndSend(MessagingConfig.TOPIC_EXCHANGE_NAME, ROUTING_KEY_GROUP_ADD, event);

    assertEventually(() -> {
      var lecturer = lecturerRepository.findByUserId(userId);
      assertThat(lecturer).isPresent();
      assertThat(lecturer.get()).satisfies(
          l -> {
            assertThat(l.getUserId()).isEqualTo(userId);
            assertThat(l.getDisplayName()).isEqualTo(user.name());
            assertThat(l.getProfile().getEmail()).isEqualTo(user.email());
            assertThat(l.getVisibleInPublicSearch()).isFalse();
          }
      );
    });
  }

  public AdminEvent createGroupAddedEvent(UUID userId, String group) {
    var event = new AdminEvent();
    event.setResourceType(ResourceType.GROUP_MEMBERSHIP);
    event.setOperationType(OperationType.CREATE);
    event.setResourcePath("users/" + userId + "/groups/" + UUID.randomUUID());
    var representation = "{\"displayName\":\"" + group + "\"}";
    event.setRepresentation(representation);
    return event;
  }
}