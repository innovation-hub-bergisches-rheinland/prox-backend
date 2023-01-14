package de.innovationhub.prox.modules.user.contract.account;

import java.util.Optional;
import java.util.UUID;

public interface UserFacade {
  Optional<ProxUserView> findById(UUID id);
}
