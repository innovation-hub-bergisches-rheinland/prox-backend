package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.profile.UserProfileMapper;
import de.innovationhub.prox.modules.user.application.profile.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateLecturerProfileHandler {

  private final UserProfileRepository userProfileRepository;
  private final UserProfileMapper userProfileMapper;

  @Transactional
  public UserProfile handle(UUID userId, CreateLecturerRequestDto dto) {
    var lecturer = this.userProfileRepository.findByUserId(userId).orElseThrow();
    
    var profile = new LecturerProfileInformation();
    userProfileMapper.updateLecturerInformationFromDto(dto.profile(), profile);

    lecturer.updateLecturerProfile(dto.visibleInPublicSearch(), profile);

    return userProfileRepository.save(lecturer);
  }
}
