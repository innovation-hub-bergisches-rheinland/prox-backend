package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindLecturerHandler {

  private final LecturerProfileRepository lecturerRepository;

  public Optional<LecturerProfile> handle(UUID id) {
    Objects.requireNonNull(id);

    return lecturerRepository.findById(id);
  }
}
