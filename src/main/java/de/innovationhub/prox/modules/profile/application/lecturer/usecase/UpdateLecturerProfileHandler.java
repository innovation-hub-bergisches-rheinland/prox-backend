package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.UpdateLecturerDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateLecturerProfileHandler {

  private final LecturerRepository lecturerRepository;
  private final AuthenticationFacade authentication;

  public Lecturer handle(UUID lecturerId, UpdateLecturerDto dto) {
    var lecturer = this.lecturerRepository.findById(lecturerId)
        .orElseThrow(() -> new RuntimeException("Lecturer could not be found"));
    if (!authentication.currentAuthenticated().equals(lecturer.getUser().getUserId())) {
      throw new RuntimeException("Not authorized"); // TODO: proper exception
    }

    lecturer.setName(dto.name());

    var profile = lecturer.getProfile();
    if (profile == null) {
      profile = new LecturerProfile();
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

    return lecturerRepository.save(lecturer);
  }
}
