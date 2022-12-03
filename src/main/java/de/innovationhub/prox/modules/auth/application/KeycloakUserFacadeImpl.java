package de.innovationhub.prox.modules.auth.application;

import de.innovationhub.prox.modules.auth.contract.UserFacade;
import de.innovationhub.prox.modules.auth.contract.UserView;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.ProcessingException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

@ApplicationComponent
public class KeycloakUserFacadeImpl implements UserFacade {

  private final RealmResource realmResource;
  private final UsersResource usersResource;

  public KeycloakUserFacadeImpl(RealmResource realmResource) {
    this.realmResource = realmResource;
    this.usersResource = realmResource.users();
  }

  public Optional<UserView> findById(UUID id) {
    try {
      var userRepresentation = this.usersResource.get(id.toString()).toRepresentation();
      return Optional.of(userRepresentation)
          .map(rep -> new UserView(UUID.fromString(rep.getId()),
              rep.getFirstName() + " " + rep.getLastName()));
    } catch (ProcessingException e) {
      if(e.getCause() instanceof javax.ws.rs.NotFoundException) {
        return Optional.empty();
      }
      throw e;
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
