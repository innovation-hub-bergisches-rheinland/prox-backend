package de.innovationhub.prox.modules.profile.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserPort {

  User save(User user);

  Optional<User> getById(UUID id);

  boolean existsById(UUID id);
}
