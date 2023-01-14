package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindLecturerHandler {

  private final LecturerRepository lecturerRepository;

  public Optional<Lecturer> handle(UUID id) {
    Objects.requireNonNull(id);

    return lecturerRepository.findById(id);
  }
}
