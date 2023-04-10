package de.innovationhub.prox.infra.keycloak;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.ProcessingException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class KeycloakClient {
  private final UsersResource usersResource;
  private final RealmResource realmResource;

  public KeycloakClient(RealmResource realmResource) {
    this.usersResource = realmResource.users();
    this.realmResource = realmResource;
  }

  @Cacheable("keycloak-user")
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

  @Cacheable("keycloak-search")
  public List<UserRepresentation> search(String query) {
    return this.realmResource.users().search(query, 0, 100, true);
  }

  @Cacheable("keycloak-users-in-role")
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

  public void joinGroup(String userId, String groupName) {
    var foundGroups = this.realmResource.groups().groups(groupName, true, 0, 1, true);
    if (foundGroups.isEmpty()) {
      throw new RuntimeException("Group %s not found".formatted(groupName));
    }
    if (foundGroups.size() > 1) {
      throw new RuntimeException("Multiple %s groups found".formatted(groupName));
    }

    usersResource.get(userId).joinGroup(foundGroups.get(0).getId());
  }

  public void getGroups(String userId) {
    this.usersResource.get(userId).groups();
  }
}
