package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
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

  @Transactional
  public UserProfile handle(UUID userId, CreateLecturerRequestDto dto) {
    var lecturer = this.userProfileRepository.findByUserId(userId).orElseThrow();

    // TODO: Use Mapstruct for that
    var profile = new LecturerProfileInformation();

    var dtoProfile = dto.profile();
    if (dtoProfile != null) {
      profile.setAffiliation(dtoProfile.affiliation());
      profile.setRoom(dtoProfile.room());
      profile.setEmail(dtoProfile.email());
      profile.setHomepage(dtoProfile.homepage());
      profile.setSubject(dtoProfile.subject());
      profile.setVita(dtoProfile.vita());
      profile.setTelephone(dtoProfile.telephone());
      profile.setPublications(dtoProfile.publications());
      profile.setCollegePage(dtoProfile.collegePage());
      profile.setConsultationHour(dtoProfile.consultationHour());
    }

    lecturer.updateLecturerProfile(dto.visibleInPublicSearch(), profile);

    return userProfileRepository.save(lecturer);
  }
}
