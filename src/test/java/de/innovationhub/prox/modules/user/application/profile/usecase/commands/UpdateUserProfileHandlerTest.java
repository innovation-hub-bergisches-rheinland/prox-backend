package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.application.profile.web.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateUserProfileHandlerTest {
  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  UpdateUserProfileHandler handler = new UpdateUserProfileHandler(userProfileRepository);

  @Test
  void shouldThrowWhenNotExists() {
    UUID userId = UUID.randomUUID();
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> handler.handle(userId, new CreateUserProfileRequestDto("displayName", "Lorem Ipsum")))
        .isInstanceOf(RuntimeException.class);

    verify(userProfileRepository).findByUserId(userId);
  }

  @Test
  void shouldSave() {
    UUID userId = UUID.randomUUID();
    var profile = createDummyProfile(userId);
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

    handler.handle(userId, new CreateUserProfileRequestDto("Xavier Tester Updated", "Lorem Ipsum Updated"));

    var captor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(captor.capture());
    assertThat(captor.getValue().getUserId()).isEqualTo(userId);
    assertThat(captor.getValue().getDisplayName()).isEqualTo("Xavier Tester Updated");
    assertThat(captor.getValue().getVita()).isEqualTo("Lorem Ipsum Updated");
  }

  private UserProfile createDummyProfile(UUID userId) {
    return UserProfile.create(userId, "Xavier Tester", "Lorem Ipsum");
  }
}