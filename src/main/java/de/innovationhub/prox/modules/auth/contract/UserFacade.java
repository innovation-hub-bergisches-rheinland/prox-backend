package de.innovationhub.prox.modules.auth.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
  Optional<UserView> findById(UUID id);
  boolean existsById(UUID id);
  List<UserView> search(String query);
}
