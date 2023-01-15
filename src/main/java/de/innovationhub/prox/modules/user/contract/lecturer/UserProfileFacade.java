package de.innovationhub.prox.modules.user.contract.lecturer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileFacade {

  Optional<UserProfileView> get(UUID id);

  List<UserProfileView> findByIds(List<UUID> ids);
}
