package de.innovationhub.prox.modules.auth.application;

import de.innovationhub.prox.modules.auth.contract.UserFacade;
import de.innovationhub.prox.modules.auth.contract.UserView;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

@RequiredArgsConstructor
@ApplicationComponent
public class KeycloakUserFacadeImpl implements UserFacade {
  private final RealmResource realmResource;
  private final UsersResource usersResource;

  public Optional<UserView> findById(UUID id) {
    try {
      return Optional.of(this.usersResource.get(id.toString()).toRepresentation())
          .map(rep -> new UserView(UUID.fromString(rep.getId()), rep.getFirstName() + " " + rep.getLastName()));
    } catch (NotFoundException e) {
      return Optional.empty();
    }
  }

  public boolean existsById(UUID id) {
    return this.findById(id).isPresent();
  }

  public List<UserView> search(String query) {
    return this.realmResource.users().search(query, 0, 100, true).stream()
        .map(u -> new UserView(UUID.fromString(u.getId()), u.getFirstName() + " " + u.getLastName()))
        .toList();
  }
}
