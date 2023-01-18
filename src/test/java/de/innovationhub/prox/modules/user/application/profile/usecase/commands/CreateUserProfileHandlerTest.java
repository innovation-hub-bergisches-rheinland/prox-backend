package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.application.profile.web.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateUserProfileHandlerTest {
  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  CreateUserProfileHandler handler = new CreateUserProfileHandler(userProfileRepository);

  @Test
  void shouldThrowWhenExists() {
    UUID userId = UUID.randomUUID();
    when(userProfileRepository.existsByUserId(userId))
        .thenReturn(true);

    assertThatThrownBy(() -> handler.handle(userId, new CreateUserProfileRequestDto("displayName", "Lorem Ipsum")))
        .isInstanceOf(RuntimeException.class);

    verify(userProfileRepository).existsByUserId(userId);
  }

  @Test
  void shouldSave() {
    UUID userId = UUID.randomUUID();
    when(userProfileRepository.existsByUserId(userId)).thenReturn(false);

    var request = new CreateUserProfileRequestDto("Xavier Tester", "Lorem Ipsum");
    handler.handle(userId, request);

    var captor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(captor.capture());
    assertThat(captor.getValue())
        .satisfies(profile -> {
          assertThat(profile.getUserId()).isEqualTo(userId);
          assertThat(profile.getDisplayName()).isEqualTo(request.displayName());
          assertThat(profile.getVita()).isEqualTo(request.vita());
        });
  }
}