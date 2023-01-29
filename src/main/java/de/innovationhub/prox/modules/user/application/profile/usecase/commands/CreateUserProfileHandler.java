package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import de.innovationhub.prox.commons.stereotypes.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDtoMapper;
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
  private final UserProfileDtoMapper userProfileDtoMapper;

  @Transactional
  public UserProfile handle(UUID userId, CreateUserProfileRequestDto request) {
    Objects.requireNonNull(userId);
    var exists = userProfileRepository.existsByUserId(userId);
    if(exists) {
      throw new RuntimeException("User profile already exists");
    }

    var contactInformation = userProfileDtoMapper.toContactInformation(request.contact());
    var profile = UserProfile.create(userId, request.displayName(), request.vita(), contactInformation);
    userProfileRepository.save(profile);
    return profile;
  }
}
