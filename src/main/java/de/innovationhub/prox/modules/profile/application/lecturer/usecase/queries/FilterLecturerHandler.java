package de.innovationhub.prox.modules.profile.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FilterLecturerHandler {

  private final LecturerRepository lecturerRepository;

  public List<Lecturer> handle(String query) {
    Objects.requireNonNull(query);

    return lecturerRepository.filter(query);
  }
}
