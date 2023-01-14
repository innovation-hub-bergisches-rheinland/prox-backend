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
import org.springframework.cache.annotation.Cacheable;

@ApplicationComponent
public class KeycloakLeveragedProxUserRepository implements ProxUserRepository {

  private final UsersResource usersResource;
  private final RealmResource realmResource;
  private final KeycloakUserMapper userMapper;

  public KeycloakLeveragedProxUserRepository(RealmResource realmResource,
      KeycloakUserMapper userMapper) {
    this.usersResource = realmResource.users();
    this.realmResource = realmResource;
    this.userMapper = userMapper;
  }

  @Override
  @Cacheable(CacheConfig.USERS)
  public Optional<ProxUser> findById(UUID id) {
    try {
      var userRepresentation = this.usersResource.get(id.toString()).toRepresentation();
      return Optional.of(userRepresentation)
          .map(this.userMapper::map);
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
    return this.userMapper.map(this.realmResource.users().search(query, 0, 100, true));
  }
}
