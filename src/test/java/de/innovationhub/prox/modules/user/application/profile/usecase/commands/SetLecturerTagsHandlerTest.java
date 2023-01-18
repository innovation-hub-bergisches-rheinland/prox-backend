package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class SetLecturerTagsHandlerTest {
  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  SetLecturerTagsHandler handler = new SetLecturerTagsHandler(userProfileRepository);

  @Test
  void shouldThrowWhenNotExists() {
    var userId = UUID.randomUUID();
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> handler.handle(userId, List.of(UUID.randomUUID())))
        .isInstanceOf(RuntimeException.class);

    verify(userProfileRepository).findByUserId(userId);
  }

  @Test
  void shouldSaveTags() {
    var userId = UUID.randomUUID();
    var tags = List.of(UUID.randomUUID());
    var lecturer = createDummyLecturer(userId);
    when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(lecturer));

    handler.handle(userId, tags);

    var profile = ArgumentCaptor.forClass(UserProfile.class);
    verify(userProfileRepository).save(profile.capture());
    assertThat(profile.getValue().getTags()).containsExactlyElementsOf(tags);
  }

  private UserProfile createDummyLecturer(UUID userId) {
    var profile = UserProfile.create(userId, "Xavier Tester", "Lorem Ipsum");
    profile.createLecturerProfile(true, createDummyProfileInformation());
    return profile;
  }

  private LecturerProfileInformation createDummyProfileInformation() {
    return new LecturerProfileInformation(
        "affiliation-old",
        "subject-old",
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
