package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class FindAllLecturerByIdsHandler {

  private final LecturerProfileRepository lecturerRepository;

  public List<LecturerProfile> handle(List<UUID> id) {
    Objects.requireNonNull(id);

    return lecturerRepository.findAllById(id);
  }
}
