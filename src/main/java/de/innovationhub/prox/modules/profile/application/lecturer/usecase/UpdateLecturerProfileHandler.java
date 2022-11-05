package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoAssembler;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class UpdateLecturerProfileHandler implements
    UseCaseHandler<LecturerDto, UpdateLecturerProfile> {

  private final LecturerRepository lecturerRepository;
  private final LecturerDtoAssembler lecturerDtoAssembler;

  @Override
  public LecturerDto handle(UpdateLecturerProfile useCase) {
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

    return lecturerDtoAssembler.toDto(lecturer);
  }
}
