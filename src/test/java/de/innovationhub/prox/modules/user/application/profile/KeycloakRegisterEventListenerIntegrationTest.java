package de.innovationhub.prox.modules.user.application.profile;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

class KeycloakRegisterEventListenerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  UserProfileRepository userProfileRepository;

  @Test
  void shouldCreateUserProfile() {
    var exchange = "amq.topic";
    var key = "KK.EVENT.CLIENT.innovation-hub-bergisches-rheinland.SUCCESS.prox-web-client.REGISTER";
    var userId = UUID.randomUUID();
    var firstName = "Xavier";
    var lastName = "Tester";
    var email = "xavier.tester@example.org";

    var event = new Event();
    event.setType(EventType.REGISTER);
    event.setRealmId("test-realm");
    event.setClientId("test-client");
    event.setUserId(userId.toString());
    event.setDetails(
        Map.of(
            "first_name", firstName,
            "last_name", lastName,
            "email", email
        )
    );

    rabbitTemplate.convertAndSend(exchange, key, event);

    await().atMost(5, SECONDS).untilAsserted(() -> {
      var userProfile = userProfileRepository.findByUserId(userId);
      assertThat(userProfile)
          .isPresent()
          .get()
          .extracting(UserProfile::getDisplayName)
          .isEqualTo("Xavier Tester");
      assertThat(userProfile.get().getContactInformation().getEmail()).isEqualTo(email);
    });
  }
}
