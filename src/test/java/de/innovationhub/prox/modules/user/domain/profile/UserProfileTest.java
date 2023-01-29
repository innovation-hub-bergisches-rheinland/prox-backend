package de.innovationhub.prox.modules.user.domain.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.innovationhub.prox.commons.buildingblocks.DomainEvent;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileAvatarSet;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileTagged;
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
    up.update("Xavier Tester 2", "Lorem Ipsum 2", new ContactInformation("Test", "Test", "Test"),
        true);
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
    up.createLecturerProfile(createDummyLecturerProfileInfo());

    assertThatThrownBy(() -> up.createLecturerProfile(createDummyLecturerProfileInfo()))
        .isInstanceOf(LecturerProfileAlreadyExistsException.class);
  }

  @Test
  void shouldRegisterLecturerProfileCreatedEvent() {
    var up = createDummyUserProfile();
    up.createLecturerProfile(createDummyLecturerProfileInfo());

    domainEventsContainOne(up, LecturerProfileCreated.class);
  }

  @Test
  void shouldThrowWhenLecturerNotExists() {
    var up = createDummyUserProfile();

    assertThatThrownBy(() -> up.updateLecturerProfile(createDummyLecturerProfileInfo()))
        .isInstanceOf(LecturerProfileDoesNotExistException.class);
  }

  @Test
  void shouldRegisterLecturerProfileUpdatedEvent() {
    var up = createDummyUserProfile();
    up.createLecturerProfile(createDummyLecturerProfileInfo());

    up.updateLecturerProfile(createDummyLecturerProfileInfo());

    domainEventsContainOne(up, LecturerProfileUpdated.class);
  }

  @Test
  void shouldRegisterLecturerTaggedEvent() {
    var up = createDummyUserProfile();
    up.createLecturerProfile(createDummyLecturerProfileInfo());

    up.tagProfile(List.of());

    domainEventsContainOne(up, UserProfileTagged.class);
  }

  private void domainEventsContainOne(UserProfile up, Class<? extends DomainEvent> domainEvent) {
    assertThat(up.getDomainEvents())
        .filteredOn(event -> domainEvent.isAssignableFrom(event.getClass()))
        .hasSize(1);
  }

  private UserProfile createDummyUserProfile() {
    return UserProfile.create(UUID.randomUUID(), "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), true);
  }

  private LecturerProfileInformation createDummyLecturerProfileInfo() {
    return new LecturerProfileInformation();
  }
}
