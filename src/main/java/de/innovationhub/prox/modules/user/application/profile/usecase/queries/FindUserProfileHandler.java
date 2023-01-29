package de.innovationhub.prox.modules.user.application.profile.usecase.queries;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindUserProfileHandler {

  private final UserProfileRepository userProfileRepository;

  public Optional<UserProfile> handle(UUID userId) {
    Objects.requireNonNull(userId);

    return userProfileRepository.findByUserId(userId);
  }
}
