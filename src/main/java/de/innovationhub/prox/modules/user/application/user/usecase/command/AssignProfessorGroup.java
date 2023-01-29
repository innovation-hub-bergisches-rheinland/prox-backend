package de.innovationhub.prox.modules.user.application.user.usecase.command;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.infra.keycloak.KeycloakConfig;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationComponent
@RequiredArgsConstructor
public class AssignProfessorGroup {
  @Autowired
  private final RealmResource resource;

  public void handle(String userId) {
    Objects.requireNonNull(userId);

    String professorGroupName = KeycloakConfig.PROFESSOR_GROUP;

    var foundGroups = resource.groups().groups(professorGroupName, true, 0, 1, true);
    if(foundGroups.isEmpty()) {
      throw new RuntimeException("Group %s not found".formatted(professorGroupName));
    }
    if(foundGroups.size() > 1) {
      throw new RuntimeException("Multiple %s groups found".formatted(professorGroupName));
    }
    var professorGroup = foundGroups.get(0);

    this.resource.users().get(userId).joinGroup(professorGroup.getId());
  }
}
