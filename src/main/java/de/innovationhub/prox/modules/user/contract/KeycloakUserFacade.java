package de.innovationhub.prox.modules.user.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KeycloakUserFacade {
  Optional<KeycloakUserView> findById(UUID id);
  boolean existsById(UUID id);
  List<KeycloakUserView> search(String query);
}
