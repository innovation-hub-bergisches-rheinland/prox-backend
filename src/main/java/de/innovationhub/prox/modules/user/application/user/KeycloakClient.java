package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.ProcessingException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
public class KeycloakClient {
  private final UsersResource usersResource;
  private final RealmResource realmResource;

  public KeycloakClient(RealmResource realmResource) {
    this.usersResource = realmResource.users();
    this.realmResource = realmResource;
  }

  @Cacheable(CacheConfig.USERS)
  public Optional<UserRepresentation> getById(String id) {
    try {
      var userRepresentation = this.usersResource.get(id).toRepresentation();
      return Optional.of(userRepresentation);
    } catch (ProcessingException e) {
      if (e.getCause() instanceof javax.ws.rs.NotFoundException) {
        return Optional.empty();
      }
      throw e;
    }
  }

  public long count() {
    return this.usersResource.count();
  }

  @Cacheable(CacheConfig.USERS_SEARCH)
  public List<UserRepresentation> search(String query) {
    return this.realmResource.users().search(query, 0, 100, true);
  }

  @Cacheable(CacheConfig.USERS_ROLE)
  public List<UserRepresentation> getAllInRole(String roleName) {
    var role = this.realmResource.roles().get(roleName);
    var paginationSize = 100;
    var users = new ArrayList<UserRepresentation>();

    for (int i = 0; ; i++) {
      var page = role.getRoleUserMembers(i * paginationSize, paginationSize);
      users.addAll(page);
      if (page.size() < paginationSize) {
        break;
      }
    }
    return users;
  }
}
