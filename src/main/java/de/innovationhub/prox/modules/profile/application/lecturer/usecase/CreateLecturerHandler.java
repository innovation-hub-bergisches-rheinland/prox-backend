package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoMapper;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerPort;
import de.innovationhub.prox.modules.profile.domain.user.UserPort;

@ApplicationComponent
public class CreateLecturerHandler implements UseCaseHandler<LecturerDto, CreateLecturer> {
  private final LecturerPort lecturerPort;
  private final UserPort userPort;
  private final LecturerDtoMapper lecturerDtoMapper;

  public CreateLecturerHandler(LecturerPort lecturerPort,
      UserPort userPort, LecturerDtoMapper lecturerDtoMapper) {
    this.lecturerPort = lecturerPort;
    this.userPort = userPort;
    this.lecturerDtoMapper = lecturerDtoMapper;
  }

  @Override
  public LecturerDto handle(CreateLecturer useCase) {
    var exists = userPort.existsById(useCase.userId());
    if(!exists) {
      throw new RuntimeException("User does not exist");
    }

    var lecturer = new Lecturer(useCase.userId(), useCase.name());
    lecturerPort.save(lecturer);

    return lecturerDtoMapper.toDto(lecturer);
  }
}
