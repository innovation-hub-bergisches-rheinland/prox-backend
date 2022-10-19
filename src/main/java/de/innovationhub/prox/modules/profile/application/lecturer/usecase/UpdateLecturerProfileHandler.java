package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoMapper;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerProfileDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateLecturerProfileHandler implements
    UseCaseHandler<LecturerProfileDto, UpdateLecturerProfile> {

  private final LecturerRepository lecturerRepository;
  private final LecturerDtoMapper lecturerDtoMapper;

  @Override
  public LecturerProfileDto handle(UpdateLecturerProfile useCase) {
    var lecturer = this.lecturerRepository.findById(useCase.lecturerId())
        .orElseThrow(() -> new RuntimeException("Lecturer could not be found"));
    var profile = new LecturerProfile(
        UUID.randomUUID(),
        useCase.affiliation(),
        useCase.subject(),
        useCase.vita(),
        useCase.publications(),
        useCase.room(),
        useCase.consultationHour(),
        useCase.email(),
        useCase.telephone(),
        useCase.homepage(),
        useCase.collegePage()
    );
    lecturer.setProfile(profile);
    lecturer = lecturerRepository.save(lecturer);

    return lecturerDtoMapper.toProfileDto(lecturer.getProfile());
  }
}
