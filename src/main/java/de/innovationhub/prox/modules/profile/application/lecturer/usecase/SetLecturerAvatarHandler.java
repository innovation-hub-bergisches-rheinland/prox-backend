package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.io.IOException;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetLecturerAvatarHandler {
  private final static String AVATAR_FILE_PREFIX = "lecturer-avatar-";

  private final StorageProvider storage;
  private final AuthenticationFacade authentication;
  private final LecturerRepository lecturerRepository;

  @Transactional
  public void handle(UUID lecturerId, byte[] avatarImageData, String contentType) {
    var lecturer = lecturerRepository.findById(lecturerId)
        .orElseThrow(() -> new RuntimeException("Lecturer not found")); // TODO: proper exception
    if(!authentication.currentAuthenticated().equals(lecturer.getUser().getUserId())) {
      throw new RuntimeException("Not authorized"); // TODO: proper exception
    }

    var fileId = AVATAR_FILE_PREFIX + lecturerId;
    try {
      storage.storeFile(fileId, avatarImageData, contentType);
      lecturer.setAvatarKey(fileId);
      lecturerRepository.save(lecturer);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store avatar image", e); // TODO: proper exception
    }
  }
}
