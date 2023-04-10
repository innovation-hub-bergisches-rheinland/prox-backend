package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.infra.aws.StorageProvider;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetUserProfileAvatarHandlerTest {

  StorageProvider storageProvider = mock(StorageProvider.class);
  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  SetUserProfileAvatarHandler handler = new SetUserProfileAvatarHandler(storageProvider,
      userProfileRepository);

  @Test
  void shouldThrowWhenUserProfileNotFound() {
    UUID userId = UUID.randomUUID();
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(userId, new byte[]{}, ""));
  }

  @Test
  void shouldSetAvatar() throws IOException {
    var userId = UUID.randomUUID();
    var userProfile = createDummyUserProfile(userId);
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

    var content = new byte[]{1, 2, 3};
    var contentType = "image/test";
    handler.handle(userId, content, contentType);

    var fileIdCapture = ArgumentCaptor.forClass(String.class);
    verify(storageProvider).storeFile(fileIdCapture.capture(), aryEq(content), eq(contentType));

    var userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(userProfileCaptor.capture());

    assertThat(userProfileCaptor.getValue())
        .satisfies(l -> assertThat(l.getAvatarKey()).isEqualTo(fileIdCapture.getValue()));
  }

  private UserProfile createDummyUserProfile(UUID userId) {
    return UserProfile.create(userId, "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
  }
}