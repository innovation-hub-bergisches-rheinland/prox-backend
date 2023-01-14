package de.innovationhub.prox.modules.user.application.lecturer.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.application.lecturer.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateLecturerProfileHandler {

  private final LecturerProfileRepository lecturerRepository;
  private final AuthenticationFacade authentication;

  @Transactional
  @PreAuthorize("@lecturerPermissionEvaluator.hasPermission(#lecturerId, authentication)")
  public LecturerProfile handle(UUID lecturerId, CreateLecturerRequestDto dto) {
    var lecturer = this.lecturerRepository.findById(lecturerId)
        .orElseThrow(
            () -> new RuntimeException("Lecturer '" + lecturerId + "' could not be found"));
    lecturer.setDisplayName(dto.name());

    var profile = lecturer.getProfile();
    if (profile == null) {
      profile = new LecturerProfileInformation();
    }

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
    lecturer.setProfile(profile);
    lecturer.setVisibleInPublicSearch(dto.visibleInPublicSearch());

    return lecturerRepository.save(lecturer);
  }
}
