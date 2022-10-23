package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoMapper;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;

@ApplicationComponent
public class CreateLecturerHandler implements UseCaseHandler<LecturerDto, CreateLecturer> {

  private final LecturerRepository lecturerRepository;
  private final LecturerDtoMapper lecturerDtoMapper;

  public CreateLecturerHandler(LecturerRepository lecturerRepository,
      LecturerDtoMapper lecturerDtoMapper) {
    this.lecturerRepository = lecturerRepository;
    this.lecturerDtoMapper = lecturerDtoMapper;
  }

  @Override
  public LecturerDto handle(CreateLecturer useCase) {
    // TODO: Maybe check with keycloak API
    var user = new UserAccount(useCase.userId());

    var lecturer = new Lecturer(user, useCase.name());
    lecturerRepository.save(lecturer);

    return lecturerDtoMapper.toDto(lecturer);
  }
}
