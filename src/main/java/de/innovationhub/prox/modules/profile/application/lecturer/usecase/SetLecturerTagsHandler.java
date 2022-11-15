package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetLecturerTagsHandler {

  private final LecturerRepository lecturerRepository;
  private final AuthenticationFacade authentication;

  public List<UUID> handle(UUID lecturerId,
      List<UUID> tags) {
    var lecturer = lecturerRepository.findById(lecturerId)
        .orElseThrow(
            () -> new RuntimeException("Lecturer not found")); // TODO: Create custom exception
    if (!authentication.currentAuthenticated().equals(lecturer.getUser().getUserId())) {
      throw new RuntimeException("Not authorized"); // TODO: proper exception
    }
    lecturer.setTags(tags);
    lecturerRepository.save(lecturer);
    return List.copyOf(lecturer.getTags());
  }
}
