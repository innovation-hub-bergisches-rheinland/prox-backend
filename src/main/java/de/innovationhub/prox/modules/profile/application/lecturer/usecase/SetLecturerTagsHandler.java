package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.modules.auth.application.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.exception.LecturerNotFoundException;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SetLecturerTagsHandler {

  private final LecturerRepository lecturerRepository;
  private final AuthenticationFacade authentication;

  @Transactional
  @PreAuthorize("@lecturerPermissionEvaluator.hasPermission(#lecturerId, authentication)")
  public List<UUID> handle(UUID lecturerId,
      List<UUID> tags) {
    var lecturer = lecturerRepository.findById(lecturerId)
        .orElseThrow(LecturerNotFoundException::new);
    if (!authentication.currentAuthenticatedId().equals(lecturer.getUser().getUserId())) {
      throw new UnauthorizedAccessException();
    }
    lecturer.setTags(tags);
    lecturerRepository.save(lecturer);
    return List.copyOf(lecturer.getTags());
  }
}
