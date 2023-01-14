package de.innovationhub.prox.modules.user.application.lecturer;

import de.innovationhub.prox.modules.user.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LecturerPermissionEvaluator {
  private final LecturerRepository lecturerRepository;

  public boolean hasPermission(Lecturer target, Authentication authentication) {
    if(authentication == null || !authentication.isAuthenticated()) return false;

    return authentication.getName().equals(target.getUserId().toString());
  }

  public boolean hasPermission(UUID lecturerId, Authentication authentication) {
    // To prevent querying the database
    if(authentication == null || !authentication.isAuthenticated()) return false;

    var lecturer = lecturerRepository.findById(lecturerId);
    // It's better to deny than throwing an exception here
    if(lecturer.isEmpty()) return false;

    return hasPermission(lecturer.get(), authentication);
  }
}
