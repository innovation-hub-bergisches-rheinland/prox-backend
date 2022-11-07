package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoAssembler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerProfileDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateLecturerProfileHandler {

  private final LecturerRepository lecturerRepository;
  private final LecturerDtoAssembler lecturerDtoAssembler;

  public LecturerDto handle(UUID lecturerId, LecturerProfileDto dto) {
    var lecturer = this.lecturerRepository.findById(lecturerId)
        .orElseThrow(() -> new RuntimeException("Lecturer could not be found"));
    var profile = new LecturerProfile(
        UUID.randomUUID(),
        dto.affiliation(),
        dto.subject(),
        dto.vita(),
        dto.publications(),
        dto.room(),
        dto.consultationHour(),
        dto.email(),
        dto.telephone(),
        dto.homepage(),
        dto.collegePage()
    );
    lecturer.setProfile(profile);
    lecturer = lecturerRepository.save(lecturer);

    return lecturerDtoAssembler.toDto(lecturer);
  }
}
