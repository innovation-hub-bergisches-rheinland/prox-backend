package de.innovationhub.prox.modules.user.application.user.usecase.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.infra.keycloak.KeycloakClient;
import de.innovationhub.prox.infra.keycloak.KeycloakConfig;
import org.junit.jupiter.api.Test;

class AssignProfessorGroupTest {

  KeycloakClient keycloakClient = mock(KeycloakClient.class);

  AssignProfessorGroup assignProfessorGroup = new AssignProfessorGroup(keycloakClient);

  @Test
  void shouldThrowOnNull() {
    assertThatThrownBy(() -> assignProfessorGroup.handle(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldAssignGroup() {
    assignProfessorGroup.handle("userId");

    verify(keycloakClient).joinGroup("userId", KeycloakConfig.PROFESSOR_GROUP);
  }
}