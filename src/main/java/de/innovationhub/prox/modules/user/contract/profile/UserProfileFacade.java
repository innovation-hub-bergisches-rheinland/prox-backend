package de.innovationhub.prox.modules.user.contract.profile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileFacade {

  Optional<UserProfileView> getByUserId(UUID id);

  List<UserProfileView> findByUserId(List<UUID> ids);

  List<UserProfileView> search(String query);
}
