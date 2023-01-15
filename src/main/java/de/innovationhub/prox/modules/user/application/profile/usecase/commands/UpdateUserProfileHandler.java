package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateUserProfileHandler {

  private final UserProfileRepository userProfileRepository;

  @Transactional
  public UserProfile handle(UUID userId, String displayName) {
    Objects.requireNonNull(userId);
    var profile = userProfileRepository.findByUserId(userId).orElseThrow();

    profile.update(displayName);
    userProfileRepository.save(profile);
    return profile;
  }
}
