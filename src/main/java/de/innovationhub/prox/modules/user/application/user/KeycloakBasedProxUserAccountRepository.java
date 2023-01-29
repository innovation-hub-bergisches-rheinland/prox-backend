package de.innovationhub.prox.modules.user.application.user;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.user.ProxUser;
import de.innovationhub.prox.modules.user.domain.user.ProxUserAccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class KeycloakBasedProxUserAccountRepository implements ProxUserAccountRepository {

  private final KeycloakUserAccountMapper userMapper;
  private final KeycloakClient keycloakClient;

  @Override
  public Optional<ProxUser> findById(UUID id) {
    return keycloakClient.getById(id.toString()).map(userMapper::map);
  }

  @Override
  public boolean existsById(UUID uuid) {
    return this.findById(uuid).isPresent();
  }

  @Override
  public long count() {
    return keycloakClient.count();
  }

  @Override
  public List<ProxUser> search(String query) {
    return keycloakClient.search(query).stream().map(userMapper::map).toList();
  }

  @Override
  public List<ProxUser> searchWithRole(String query, String roleName) {
    var users = keycloakClient.getAllInRole(roleName);
    // TODO: refine search query
    var words = List.of(query.split("\\s+"));
    return users.stream().filter(user -> words.stream().allMatch(
        word -> user.getFirstName().toLowerCase().contains(word.toLowerCase()) || user.getLastName()
            .toLowerCase().contains(word.toLowerCase()) || user.getEmail().toLowerCase()
            .contains(word.toLowerCase()) || user.getUsername().toLowerCase()
            .contains(word.toLowerCase()))).map(userMapper::map).toList();
  }
}
