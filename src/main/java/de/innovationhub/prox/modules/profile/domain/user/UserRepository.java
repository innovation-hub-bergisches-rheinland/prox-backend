package de.innovationhub.prox.modules.profile.domain.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {

  Optional<User> getById(UUID id);
}
