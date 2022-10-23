package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoMapper;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.profile.domain.user.UserRepository;

@ApplicationComponent
public class CreateLecturerHandler implements UseCaseHandler<LecturerDto, CreateLecturer> {

  private final LecturerRepository lecturerRepository;
  private final UserRepository userRepository;
  private final LecturerDtoMapper lecturerDtoMapper;

  public CreateLecturerHandler(LecturerRepository lecturerRepository,
      UserRepository userRepository, LecturerDtoMapper lecturerDtoMapper) {
    this.lecturerRepository = lecturerRepository;
    this.userRepository = userRepository;
    this.lecturerDtoMapper = lecturerDtoMapper;
  }

  @Override
  public LecturerDto handle(CreateLecturer useCase) {
    var user = userRepository.findById(useCase.userId())
        .orElseThrow(() -> new RuntimeException("User does not exist"));

    var lecturer = new Lecturer(user, useCase.name());
    lecturerRepository.save(lecturer);

    return lecturerDtoMapper.toDto(lecturer);
  }
}
