package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDto;
import de.innovationhub.prox.modules.profile.application.lecturer.dto.LecturerDtoAssembler;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.profile.domain.user.UserAccount;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class CreateLecturerHandler {

  private final LecturerRepository lecturerRepository;
  private final LecturerDtoAssembler lecturerDtoAssembler;
  private final AuthenticationFacade authenticationFacade;

  public LecturerDto handle(String name) {
    var user = new UserAccount(authenticationFacade.currentAuthenticated());

    var lecturer = new Lecturer(user, name);
    lecturerRepository.save(lecturer);

    return lecturerDtoAssembler.toDto(lecturer);
  }
}
