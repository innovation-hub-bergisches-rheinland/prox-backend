package de.innovationhub.prox.modules.profile.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationComponent
@RequiredArgsConstructor
public class FilterLecturerHandler {

  private final LecturerRepository lecturerRepository;

  public Page<Lecturer> handle(String query, Pageable pageable) {
    Objects.requireNonNull(query);

    return lecturerRepository.filter(query, pageable);
  }
}
