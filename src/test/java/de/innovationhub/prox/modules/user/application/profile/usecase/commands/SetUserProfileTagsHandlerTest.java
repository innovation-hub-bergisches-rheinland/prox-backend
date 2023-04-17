package de.innovationhub.prox.modules.user.application.profile.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.tag.contract.TagCollectionFacade;
import de.innovationhub.prox.modules.tag.contract.dto.TagCollectionDto;
import de.innovationhub.prox.modules.user.domain.profile.ContactInformation;
import de.innovationhub.prox.modules.user.domain.profile.LecturerProfileInformation;
import de.innovationhub.prox.modules.user.domain.profile.UserProfile;
import de.innovationhub.prox.modules.user.domain.profile.UserProfileRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetUserProfileTagsHandlerTest {

  UserProfileRepository userProfileRepository = mock(UserProfileRepository.class);
  TagCollectionFacade tagCollectionFacade = mock(TagCollectionFacade.class);
  SetUserProfileTagsHandler handler = new SetUserProfileTagsHandler(userProfileRepository, tagCollectionFacade);

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
    var tagCollectionId = UUID.randomUUID();
    lecturer.setTagCollectionId(tagCollectionId);
    when(tagCollectionFacade.setTagCollection(eq(tagCollectionId), anyList())).thenReturn(new TagCollectionDto(
        tagCollectionId,
        List.of()
    ));

    handler.handle(userId, tags);

    verify(tagCollectionFacade).setTagCollection(eq(tagCollectionId), assertArg(t -> assertThat(t).containsExactlyElementsOf(tags)));
    verify(userProfileRepository).save(assertArg(p -> assertThat(p.getTagCollectionId()).isEqualTo(tagCollectionId)));
  }

  private UserProfile createDummyLecturer(UUID userId) {
    var profile = UserProfile.create(userId, "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
    profile.createLecturerProfile(createDummyProfileInformation());
    return profile;
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
