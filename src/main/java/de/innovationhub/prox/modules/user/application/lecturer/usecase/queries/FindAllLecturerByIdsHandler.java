package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllLecturerByIdsHandler {

  private final LecturerRepository lecturerRepository;

  public List<Lecturer> handle(List<UUID> id) {
    Objects.requireNonNull(id);

    return lecturerRepository.findAllById(id);
  }
}
