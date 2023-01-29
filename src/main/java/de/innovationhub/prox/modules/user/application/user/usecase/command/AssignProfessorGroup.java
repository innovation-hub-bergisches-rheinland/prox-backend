package de.innovationhub.prox.modules.user.application.user.usecase.command;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.infra.keycloak.KeycloakClient;
import de.innovationhub.prox.infra.keycloak.KeycloakConfig;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationComponent
@RequiredArgsConstructor
public class AssignProfessorGroup {

  @Autowired
  private final KeycloakClient keycloakClient;

  public void handle(String userId) {
    Objects.requireNonNull(userId);

    String professorGroupName = KeycloakConfig.PROFESSOR_GROUP;

    this.keycloakClient.joinGroup(userId, professorGroupName);
  }
}
