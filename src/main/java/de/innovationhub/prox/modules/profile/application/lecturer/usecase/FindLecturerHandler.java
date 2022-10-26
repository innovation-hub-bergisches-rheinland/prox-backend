package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.commons.application.usecase.UseCaseHandler;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindLecturerHandler implements UseCaseHandler<Lecturer, FindLecturer> {
  private final LecturerRepository lecturerRepository;

  @Override
  public Lecturer handle(FindLecturer useCase) {
    return lecturerRepository.findById(useCase.id()).orElseThrow();
  }
}
