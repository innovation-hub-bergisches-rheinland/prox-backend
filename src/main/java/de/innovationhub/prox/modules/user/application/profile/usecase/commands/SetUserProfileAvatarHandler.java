package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.commons.application.ApplicationComponent;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationComponent
@RequiredArgsConstructor
public class SetUserProfileAvatarHandler {

  private final static String AVATAR_FILE_PREFIX = "avatar-";

  private final StorageProvider storage;
  private final UserProfileRepository userProfileRepository;

  @Transactional
  public void handle(UUID userId, byte[] avatarImageData, String contentType) {
    var user = userProfileRepository.findByUserId(userId).orElseThrow();

    var fileId = AVATAR_FILE_PREFIX + user.getUserId();
    try {
      storage.storeFile(fileId, avatarImageData, contentType);
      user.setAvatarKey(fileId);
      userProfileRepository.save(user);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store avatar image", e);
    }
  }
}
