package de.innovationhub.prox.profile.user;

import java.util.Optional;
import java.util.UUID;

public interface UserPort {
  User save(User user);
  Optional<User> getById(UUID id);
}
