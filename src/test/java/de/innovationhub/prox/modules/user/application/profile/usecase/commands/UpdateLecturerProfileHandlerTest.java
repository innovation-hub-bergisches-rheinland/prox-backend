package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.application.profile.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.user.application.profile.web.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateLecturerProfileHandlerTest {
  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  UpdateLecturerProfileHandler handler = new UpdateLecturerProfileHandler(userProfileRepository);

  @Test
  void shouldThrowWhenNotFound() {
    when(userProfileRepository.findByUserId(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> handler.handle(UUID.randomUUID(), createLecturerRequestDto()))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldSaveLecturer() {
    UUID userId = UUID.randomUUID();
    var userProfile = createDummyLecturer(userId);
    userProfile.createLecturerProfile(true, createDummyProfileInformation());
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

    var request = createLecturerRequestDto();
    handler.handle(userId, request);

    var captor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(captor.capture());
    assertThat(captor.getValue().getLecturerProfile().getProfile())
        .satisfies(lp -> {
          assertThat(lp.getAffiliation()).isEqualTo(request.profile().affiliation());
          assertThat(lp.getSubject()).isEqualTo(request.profile().subject());
          assertThat(lp.getVita()).isEqualTo(request.profile().vita());
          assertThat(lp.getPublications()).containsExactlyInAnyOrderElementsOf(request.profile().publications());
          assertThat(lp.getRoom()).isEqualTo(request.profile().room());
          assertThat(lp.getConsultationHour()).isEqualTo(request.profile().consultationHour());
          assertThat(lp.getEmail()).isEqualTo(request.profile().email());
          assertThat(lp.getTelephone()).isEqualTo(request.profile().telephone());
          assertThat(lp.getHomepage()).isEqualTo(request.profile().homepage());
          assertThat(lp.getCollegePage()).isEqualTo(request.profile().collegePage());
        });
  }

  private CreateLecturerRequestDto createLecturerRequestDto() {
    return new CreateLecturerRequestDto(
        new CreateLecturerProfileDto(
            "affiliation",
            "subject",
            "vita",
            List.of("publication"),
            "room",
            "consultationHour",
            "email",
            "telephone",
            "homepage",
            "collegePage"
        ),
        false
    );
  }

  private UserProfile createDummyLecturer(UUID userId) {
    return UserProfile.create(userId, "Xavier Tester");
  }

  private LecturerProfileInformation createDummyProfileInformation() {
    return new LecturerProfileInformation(
        "affiliation-old",
        "subject-old",
        "vita-old",
        List.of("publication-old"),
        "room-old",
        "consultationHour-old",
        "email-old",
        "telephone-old",
        "homepage-old",
        "collegePage-old"
    );
  }
}