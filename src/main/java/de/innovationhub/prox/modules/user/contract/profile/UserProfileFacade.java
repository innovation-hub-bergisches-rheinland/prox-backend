package de.innovationhub.prox.modules.user.contract.profile;

import de.innovationhub.prox.modules.user.contract.profile.dto.UserProfileDto;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileFacade {

  Optional<UserProfileDto> getByUserId(UUID id);

  List<UserProfileDto> findByUserIds(List<UUID> ids);

  List<UserProfileDto> search(String query);
  List<UserProfileDto> findLecturersByIds(Collection<UUID> ids);
}
