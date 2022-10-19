package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoMapper;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerProfileDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerPort;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfile;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateLecturerProfileHandler implements UseCaseHandler<LecturerProfileDto, UpdateLecturerProfile> {
  private final LecturerPort lecturerPort;
  private final LecturerDtoMapper lecturerDtoMapper;
  @Override
  public LecturerProfileDto handle(UpdateLecturerProfile useCase) {
    var lecturer = this.lecturerPort.getById(useCase.lecturerId())
        .orElseThrow(() -> new RuntimeException("Lecturer could not be found"));
    var profile = new LecturerProfile(
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
    lecturer = lecturerPort.save(lecturer);

    return lecturerDtoMapper.toProfileDto(lecturer.getProfile());
  }
}
