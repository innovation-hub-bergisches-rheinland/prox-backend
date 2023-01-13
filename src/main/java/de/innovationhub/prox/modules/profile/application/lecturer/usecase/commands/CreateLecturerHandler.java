package de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfileInformation;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class CreateLecturerHandler {

  private final LecturerRepository lecturerRepository;
  private final AuthenticationFacade authenticationFacade;

  @Transactional
  public Lecturer handle(UUID userId, CreateLecturerRequestDto dto) {
    Objects.requireNonNull(dto);

    var lecturer = Lecturer.create(userId, dto.name());
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
    lecturer.setProfile(profile);
    lecturer.setVisibleInPublicSearch(dto.visibleInPublicSearch());

    return lecturerRepository.save(lecturer);
  }
}
