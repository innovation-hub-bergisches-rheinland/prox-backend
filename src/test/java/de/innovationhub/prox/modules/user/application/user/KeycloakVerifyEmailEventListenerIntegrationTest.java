package de.innovationhub.prox.modules.user.application.user;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.infra.keycloak.KeycloakClient;
import de.innovationhub.prox.infra.keycloak.KeycloakConfig;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@Disabled("""
      Flaky because KeycloakClient is being mocked. Needs to be refactored to use a real Keycloak instance.
      Works when running the test individually.
      Some more details: https://stackoverflow.com/questions/75111282/using-spring-boot-mockbean-annotation-in-rabbitmq-listener
    """)
class KeycloakVerifyEmailEventListenerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @MockBean
  KeycloakClient keycloakClient;

  @Test
  void shouldAddToGroup() {
    var userId = UUID.randomUUID();
    sendVerifyEmailEvent(userId, "xavier.tester@th-koeln.de");

    await().atMost(5, SECONDS).untilAsserted(() -> {
      verify(keycloakClient).joinGroup(userId.toString(), KeycloakConfig.PROFESSOR_GROUP);
    });
  }

  private void sendVerifyEmailEvent(UUID userId, String email) {
    var exchange = "amq.topic";
    var key = "KK.EVENT.CLIENT.innovation-hub-bergisches-rheinland.SUCCESS.prox-web-client.VERIFY_EMAIL";

    var event = new Event();
    event.setType(EventType.VERIFY_EMAIL);
    event.setRealmId("test-realm");
    event.setClientId("test-client");
    event.setUserId(userId.toString());
    event.setDetails(
        Map.of(
            "email", email
        )
    );

    rabbitTemplate.convertAndSend(exchange, key, event);
  }
}
