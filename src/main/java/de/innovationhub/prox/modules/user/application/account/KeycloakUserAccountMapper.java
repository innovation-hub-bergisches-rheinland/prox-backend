package de.innovationhub.prox.modules.user.application.account;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.account.ProxUserAccount;
import de.innovationhub.prox.modules.user.domain.account.StandardUserAccount;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationComponent
public class KeycloakUserAccountMapper {

  public ProxUserAccount map(UserRepresentation keycloakUser) {
    Objects.requireNonNull(keycloakUser);

    return new StandardUserAccount(UUID.fromString(keycloakUser.getId()), extractName(keycloakUser),
        keycloakUser.getEmail());
  }

  public List<ProxUserAccount> map(List<UserRepresentation> keycloakUsers) {
    return keycloakUsers.stream().map(this::map).toList();
  }

  private String extractName(UserRepresentation rep) {
    return rep.getFirstName() + " " + rep.getLastName();
  }
}
