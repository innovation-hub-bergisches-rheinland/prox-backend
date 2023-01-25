package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateUserProfileRequestDto.ContactInformationRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.UserProfileDtoMapper;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateUserProfileHandlerTest {

  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  CreateUserProfileHandler handler = new CreateUserProfileHandler(userProfileRepository,
      UserProfileDtoMapper.INSTANCE);

  @Test
  void shouldThrowWhenExists() {
    UUID userId = UUID.randomUUID();
    when(userProfileRepository.existsByUserId(userId))
        .thenReturn(true);

    assertThatThrownBy(() -> handler.handle(userId,
            new CreateUserProfileRequestDto("displayName", "Lorem Ipsum",
                new ContactInformationRequestDto("Test", "Test", "Test"))))
        .isInstanceOf(RuntimeException.class);

    verify(userProfileRepository).existsByUserId(userId);
  }

  @Test
  void shouldSave() {
    UUID userId = UUID.randomUUID();
    when(userProfileRepository.existsByUserId(userId)).thenReturn(false);

    var request = new CreateUserProfileRequestDto("Xavier Tester", "Lorem Ipsum",
        new ContactInformationRequestDto("Test", "Test", "Test"));
    handler.handle(userId, request);

    var captor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(captor.capture());
    assertThat(captor.getValue())
        .satisfies(profile -> {
          assertThat(profile.getUserId()).isEqualTo(userId);
          assertThat(profile.getDisplayName()).isEqualTo(request.displayName());
          assertThat(profile.getVita()).isEqualTo(request.vita());
          assertThat(profile.getContactInformation()).satisfies(contactInformation -> {
            assertThat(contactInformation.getTelephone()).isEqualTo(request.contact().telephone());
            assertThat(contactInformation.getEmail()).isEqualTo(request.contact().email());
            assertThat(contactInformation.getHomepage()).isEqualTo(request.contact().homepage());
          });
        });
  }
}