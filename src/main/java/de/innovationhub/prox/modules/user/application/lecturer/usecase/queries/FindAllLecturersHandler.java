package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllLecturersHandler {

  private final LecturerRepository lecturerRepository;

  public Page<Lecturer> handle(Pageable pageable) {
    return lecturerRepository.findAll(pageable);
  }
}
