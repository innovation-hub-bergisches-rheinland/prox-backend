package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoMapper;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class CreateLecturerHandler implements UseCaseHandler<LecturerDto, CreateLecturer> {

  private final LecturerRepository lecturerRepository;
  private final LecturerDtoMapper lecturerDtoMapper;
  private final AuthenticationFacade authenticationFacade;

  @Override
  public LecturerDto handle(CreateLecturer useCase) {
    var user = new UserAccount(authenticationFacade.currentAuthenticated());

    var lecturer = new Lecturer(user, useCase.name());
    lecturerRepository.save(lecturer);

    return lecturerDtoMapper.toDto(lecturer);
  }
}
