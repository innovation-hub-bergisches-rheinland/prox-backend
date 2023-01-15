package de.innovationhub.prox.modules.user.domain.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.innovationhub.prox.modules.commons.domain.DomainEvent;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileTagged;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileAvatarSet;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileUpdated;
import de.innovationhub.prox.modules.user.domain.profile.exception.LecturerProfileAlreadyExistsException;
import de.innovationhub.prox.modules.user.domain.profile.exception.LecturerProfileDoesNotExistException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserProfileTest {

  @Test
  void shouldRegisterCreatedEventOnCreate() {
    var up = createDummyUserProfile();
    domainEventsContainOne(up, UserProfileCreated.class);
  }

  @Test
  void shouldRegisterUpdateEvent() {
    var up = createDummyUserProfile();
    up.update("Xavier Tester 2");
    domainEventsContainOne(up, UserProfileUpdated.class);
  }

  @Test
  void shouldRegisterAvatarEvent() {
    var up = createDummyUserProfile();
    up.setAvatarKey("avatar");
    domainEventsContainOne(up, UserProfileAvatarSet.class);
  }

  @Test
  void shouldThrowWhenLecturerAlreadyExists() {
    var up = createDummyUserProfile();
    up.createLecturerProfile(false, createDummyLecturerProfileInfo());

    assertThatThrownBy(() -> up.createLecturerProfile(false, createDummyLecturerProfileInfo()))
        .isInstanceOf(LecturerProfileAlreadyExistsException.class);
  }

  @Test
  void shouldRegisterLecturerProfileCreatedEvent() {
    var up = createDummyUserProfile();
    up.createLecturerProfile(false, createDummyLecturerProfileInfo());

    domainEventsContainOne(up, LecturerProfileCreated.class);
  }

  @Test
  void shouldThrowWhenLecturerNotExists() {
    var up = createDummyUserProfile();

    assertThatThrownBy(() -> up.updateLecturerProfile(false, createDummyLecturerProfileInfo()))
        .isInstanceOf(LecturerProfileDoesNotExistException.class);
  }

  @Test
  void shouldRegisterLecturerProfileUpdatedEvent() {
    var up = createDummyUserProfile();
    up.createLecturerProfile(false, createDummyLecturerProfileInfo());

    up.updateLecturerProfile(false, createDummyLecturerProfileInfo());

    domainEventsContainOne(up, LecturerProfileUpdated.class);
  }

  @Test
  void shouldThrowWhenLecturerProfileNotExistsOnTag() {
    var up = createDummyUserProfile();

    assertThatThrownBy(() -> up.tagLecturerProfile(List.of()))
        .isInstanceOf(LecturerProfileDoesNotExistException.class);
  }

  @Test
  void shouldRegisterLecturerTaggedEvent() {
    var up = createDummyUserProfile();
    up.createLecturerProfile(false, createDummyLecturerProfileInfo());

    up.tagLecturerProfile(List.of());

    domainEventsContainOne(up, LecturerProfileTagged.class);
  }

  private void domainEventsContainOne(UserProfile up, Class<? extends DomainEvent> domainEvent) {
    assertThat(up.getDomainEvents())
        .filteredOn(event -> domainEvent.isAssignableFrom(event.getClass()))
        .hasSize(1);
  }

  private UserProfile createDummyUserProfile() {
    return UserProfile.create(UUID.randomUUID(), "Xavier Tester");
  }

  private LecturerProfileInformation createDummyLecturerProfileInfo() {
    return new LecturerProfileInformation();
  }
}
