package de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands;

import de.innovationhub.prox.infra.aws.s3.StorageProvider;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.application.lecturer.exception.LecturerNotFoundException;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.application.user.exception.UnauthorizedAccessException;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@ApplicationComponent
@RequiredArgsConstructor
public class SetLecturerAvatarHandler {
  private final static String AVATAR_FILE_PREFIX = "lecturer-avatar-";

  private final StorageProvider storage;
  private final AuthenticationFacade authentication;
  private final LecturerRepository lecturerRepository;

  @Transactional
  @PreAuthorize("@lecturerPermissionEvaluator.hasPermission(#lecturerId, authentication)")
  public void handle(UUID lecturerId, byte[] avatarImageData, String contentType) {
    var lecturer = lecturerRepository.findById(lecturerId)
        .orElseThrow(LecturerNotFoundException::new);
    if(!authentication.currentAuthenticatedId().equals(lecturer.getUserId())) {
      throw new UnauthorizedAccessException();
    }

    var fileId = AVATAR_FILE_PREFIX + lecturerId;
    try {
      storage.storeFile(fileId, avatarImageData, contentType);
      lecturer.setAvatarKey(fileId);
      lecturerRepository.save(lecturer);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store avatar image", e);
    }
  }
}
