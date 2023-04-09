package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllLecturersByIdsHandler {

  private final UserProfileRepository userProfileRepository;

  public List<UserProfile> handle(Collection<UUID> id) {
    Objects.requireNonNull(id);

    return userProfileRepository.findAllLecturersByIds(id, Pageable.unpaged())
        .stream().toList();
  }
}
