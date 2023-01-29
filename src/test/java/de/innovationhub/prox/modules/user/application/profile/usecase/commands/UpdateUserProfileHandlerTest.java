package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto.ContactInformationRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDtoMapper;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateUserProfileHandlerTest {

  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  UpdateUserProfileHandler handler = new UpdateUserProfileHandler(userProfileRepository,
      UserProfileDtoMapper.INSTANCE);

  @Test
  void shouldThrowWhenNotExists() {
    UUID userId = UUID.randomUUID();
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> handler.handle(userId,
        new CreateUserProfileRequestDto("displayName", "Lorem Ipsum",
            new ContactInformationRequestDto("Test", "Test", "Test"), true)))
        .isInstanceOf(RuntimeException.class);

    verify(userProfileRepository).findByUserId(userId);
  }

  @Test
  void shouldSave() {
    UUID userId = UUID.randomUUID();
    var profile = createDummyProfile(userId);
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

    handler.handle(userId,
        new CreateUserProfileRequestDto("Xavier Tester Updated", "Lorem Ipsum Updated",
            new ContactInformationRequestDto("Test", "Test", "Test"), true));

    var captor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(captor.capture());
    assertThat(captor.getValue().getUserId()).isEqualTo(userId);
    assertThat(captor.getValue().getDisplayName()).isEqualTo("Xavier Tester Updated");
    assertThat(captor.getValue().getVita()).isEqualTo("Lorem Ipsum Updated");
    assertThat(captor.getValue().getContactInformation().getHomepage()).isEqualTo("Test");
    assertThat(captor.getValue().getContactInformation().getTelephone()).isEqualTo("Test");
    assertThat(captor.getValue().getContactInformation().getEmail()).isEqualTo("Test");
  }

  private UserProfile createDummyProfile(UUID userId) {
    return UserProfile.create(userId, "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
  }
}