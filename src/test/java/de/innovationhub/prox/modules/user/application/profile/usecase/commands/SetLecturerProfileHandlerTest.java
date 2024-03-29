package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.application.profile.UserProfileMapper;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.user.application.profile.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetLecturerProfileHandlerTest {

  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  SetLecturerProfileHandler handler = new SetLecturerProfileHandler(userProfileRepository,
      UserProfileMapper.INSTANCE);

  @Test
  void shouldThrowWhenNotFound() {
    when(userProfileRepository.findByUserId(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> handler.handle(UUID.randomUUID(), createLecturerRequestDto()))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldCreateLecturer() {
    UUID userId = UUID.randomUUID();
    var userProfile = createDummyLecturer(userId);
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

    var request = createLecturerRequestDto();
    handler.handle(userId, request);

    var captor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(captor.capture());
    assertThat(captor.getValue().getLecturerProfile().getProfile())
        .satisfies(lp -> {
          assertThat(lp.getAffiliation()).isEqualTo(request.profile().affiliation());
          assertThat(lp.getSubject()).isEqualTo(request.profile().subject());
          assertThat(lp.getPublications()).containsExactlyInAnyOrderElementsOf(
              request.profile().publications());
          assertThat(lp.getRoom()).isEqualTo(request.profile().room());
          assertThat(lp.getConsultationHour()).isEqualTo(request.profile().consultationHour());
          assertThat(lp.getCollegePage()).isEqualTo(request.profile().collegePage());
        });
  }

  @Test
  void shouldUpdateLecturer() {
    UUID userId = UUID.randomUUID();
    var userProfile = createDummyLecturer(userId);
    userProfile.createLecturerProfile(createDummyProfileInformation());
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

    var request = createLecturerRequestDto();
    handler.handle(userId, request);

    var captor = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(captor.capture());
    assertThat(captor.getValue().getLecturerProfile().getProfile())
        .satisfies(lp -> {
          assertThat(lp.getAffiliation()).isEqualTo(request.profile().affiliation());
          assertThat(lp.getSubject()).isEqualTo(request.profile().subject());
          assertThat(lp.getPublications()).containsExactlyInAnyOrderElementsOf(
              request.profile().publications());
          assertThat(lp.getRoom()).isEqualTo(request.profile().room());
          assertThat(lp.getConsultationHour()).isEqualTo(request.profile().consultationHour());
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
            "collegePage"
        )
    );
  }

  private UserProfile createDummyLecturer(UUID userId) {
    return UserProfile.create(userId, "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("email", "telephone", "homepage"), true);
  }

  private LecturerProfileInformation createDummyProfileInformation() {
    return new LecturerProfileInformation(
        "affiliation-old",
        "subject-old",
        List.of("publication-old"),
        "room-old",
        "consultationHour-old",
        "collegePage-old"
    );
  }
}