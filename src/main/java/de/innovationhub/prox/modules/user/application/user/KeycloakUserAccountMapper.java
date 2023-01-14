package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationComponent
public class KeycloakUserAccountMapper {

  public ProxUser map(UserRepresentation keycloakUser) {
    Objects.requireNonNull(keycloakUser);

    return new ProxUser(UUID.fromString(keycloakUser.getId()), extractName(keycloakUser),
        keycloakUser.getEmail());
  }

  public List<ProxUser> map(List<UserRepresentation> keycloakUsers) {
    return keycloakUsers.stream().map(this::map).toList();
  }

  private String extractName(UserRepresentation rep) {
    return rep.getFirstName() + " " + rep.getLastName();
  }
}
