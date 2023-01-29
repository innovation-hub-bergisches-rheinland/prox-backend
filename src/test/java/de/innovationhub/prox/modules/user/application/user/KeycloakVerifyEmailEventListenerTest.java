package de.innovationhub.prox.modules.user.application.user;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.user.application.user.usecase.command.AssignProfessorGroup;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.events.Event;
import org.keycloak.events.EventType;

class KeycloakVerifyEmailEventListenerTest extends AbstractIntegrationTest {

  AssignProfessorGroup assignProfessorGroup = mock(AssignProfessorGroup.class);
  KeycloakVerifyEmailEventListener keycloakVerifyEmailEventListener = new KeycloakVerifyEmailEventListener(assignProfessorGroup);

  @ParameterizedTest
  @ValueSource(strings = {"th-koeln.de", "fh-koeln.de"})
  void shouldAddToGroup(String domain) {
    var userId = UUID.randomUUID();
    var event = createEvent(userId, "xavier.tester@" + domain);

    keycloakVerifyEmailEventListener.onVerifyEmailEvent(event);

    verify(assignProfessorGroup).handle(userId.toString());
  }

  @ParameterizedTest
  @ValueSource(strings = { "example.org", "smail.th-koeln.de", "smail.fh-koeln.de"})
  void shouldNotAddToGroup(String domain) {
    var userId = UUID.randomUUID();
    var event = createEvent(userId, "xavier.tester@" + domain);

    keycloakVerifyEmailEventListener.onVerifyEmailEvent(event);

    verify(assignProfessorGroup, times(0)).handle(userId.toString());
  }

  private Event createEvent(UUID userId, String email) {
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

    return event;
  }
}
