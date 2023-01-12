package de.innovationhub.prox.modules.auth.application;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.auth.contract.KeycloakUserFacade;
import de.innovationhub.prox.modules.auth.contract.KeycloakUserView;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.ProcessingException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
public class KeycloakKeycloakUserFacadeImpl implements KeycloakUserFacade {

  private final RealmResource realmResource;
  private final UsersResource usersResource;

  public KeycloakKeycloakUserFacadeImpl(RealmResource realmResource) {
    this.realmResource = realmResource;
    this.usersResource = realmResource.users();
  }

  @Cacheable(CacheConfig.USERS)
  public Optional<KeycloakUserView> findById(UUID id) {
    try {
      var userRepresentation = this.usersResource.get(id.toString()).toRepresentation();
      return Optional.of(userRepresentation)
          .map(rep -> new KeycloakUserView(UUID.fromString(rep.getId()),
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

  @Cacheable(CacheConfig.USERS)
  public List<KeycloakUserView> search(String query) {
    return this.realmResource.users().search(query, 0, 100, true).stream()
        .map(u -> new KeycloakUserView(UUID.fromString(u.getId()), u.getFirstName() + " " + u.getLastName()))
        .toList();
  }
}
