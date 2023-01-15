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
public class CreateUserProfileHandler {

  private final UserProfileRepository userProfileRepository;

  @Transactional
  public UserProfile handle(UUID userId, String displayName) {
    Objects.requireNonNull(userId);
    var exists = userProfileRepository.existsByUserId(userId);
    if(exists) {
      throw new RuntimeException("User profile already exists");
    }

    var profile = UserProfile.create(userId, displayName);
    userProfileRepository.save(profile);
    return profile;
  }
}
