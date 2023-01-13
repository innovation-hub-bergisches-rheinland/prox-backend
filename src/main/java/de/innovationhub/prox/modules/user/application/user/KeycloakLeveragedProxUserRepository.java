package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.config.CacheConfig;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import de.innovationhub.prox.modules.user.domain.user.ProxUserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.ProcessingException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
public class KeycloakLeveragedProxUserRepository implements ProxUserRepository {
  private final UsersResource usersResource;
  private final RealmResource realmResource;

  public KeycloakLeveragedProxUserRepository(RealmResource realmResource) {
    this.usersResource = realmResource.users();
    this.realmResource = realmResource;
  }

  @Override
  @Cacheable(CacheConfig.USERS)
  public Optional<ProxUser> findById(UUID id) {
    try {
      var userRepresentation = this.usersResource.get(id.toString()).toRepresentation();
      return Optional.of(userRepresentation)
          .map(rep -> new ProxUser(UUID.fromString(rep.getId()), extractName(rep)));
    } catch (ProcessingException e) {
      if(e.getCause() instanceof javax.ws.rs.NotFoundException) {
        return Optional.empty();
      }
      throw e;
    }
  }

  @Override
  public boolean existsById(UUID uuid) {
    return this.findById(uuid).isPresent();
  }

  @Override
  public long count() {
    return this.usersResource.count();
  }

  @Override
  public List<ProxUser> search(String query) {
    return this.realmResource.users().search(query, 0, 100, true).stream()
        .map(u -> new ProxUser(UUID.fromString(u.getId()), extractName(u)))
        .toList();
  }

  private String extractName(UserRepresentation rep) {
    return rep.getFirstName() + " " + rep.getLastName();
  }
}
