package de.innovationhub.prox.modules.profile.domain.lecturer;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerAvatarSet;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerCreated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerRenamed;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerTagged;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LecturerTest {
  private Lecturer createDummyLecturer() {
    return Lecturer.create(UUID.randomUUID(), "Max Mustermann");
  }

  @Test
  void shouldCreateLecturer() {
    var lecturer = createDummyLecturer();

    assertThat(lecturer.getDomainEvents())
        .hasSize(1)
        .first()
        .isInstanceOf(LecturerCreated.class);
  }

  @Test
  void shouldSetTags() {
    var lecturer = createDummyLecturer();
    var tag = UUID.randomUUID();

    lecturer.setTags(List.of(tag));

    assertThat(lecturer.getTags()).containsExactly(tag);
    assertThat(lecturer.getDomainEvents())
        .filteredOn(e -> e instanceof LecturerTagged)
        .first()
        .isInstanceOfSatisfying(
            LecturerTagged.class,
            e -> {
              assertThat(e.lecturerId()).isEqualTo(lecturer.getId());
              assertThat(e.tags()).containsExactly(tag);
            });
  }

  @Test
  void shouldUpdateProfile() {
    var lecturer = createDummyLecturer();
    var profile = new LecturerProfileInformation();

    lecturer.setProfile(profile);

    assertThat(lecturer.getProfile()).isEqualTo(profile);
    assertThat(lecturer.getDomainEvents())
        .filteredOn(e -> e instanceof LecturerProfileUpdated)
        .hasSize(1);
  }

  @Test
  void shouldNotChangeOnUnchangedName() {
    var lecturer = createDummyLecturer();

    // Same as the dummy lecturer
    lecturer.setDisplayName("Max Mustermann");

    // Unchanged
    assertThat(lecturer.getDisplayName()).isEqualTo("Max Mustermann");
    assertThat(lecturer.getDomainEvents())
        .filteredOn(e -> e instanceof LecturerRenamed)
        .isEmpty();
  }

  @Test
  void shouldSetName() {
    var lecturer = createDummyLecturer();

    lecturer.setDisplayName("Erika Musterfrau");

    assertThat(lecturer.getDisplayName()).isEqualTo("Erika Musterfrau");
    assertThat(lecturer.getDomainEvents())
        .filteredOn(e -> e instanceof LecturerRenamed)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(
            LecturerRenamed.class,
            e -> {
              assertThat(e.lecturerId()).isEqualTo(lecturer.getId());
              assertThat(e.name()).isEqualTo("Erika Musterfrau");
            });
  }

  @Test
  void shouldSetAvatarKey() {
    var lecturer = createDummyLecturer();
    var key = UUID.randomUUID().toString();

    lecturer.setAvatarKey(key);

    assertThat(lecturer.getAvatarKey()).isEqualTo(key);
    assertThat(lecturer.getDomainEvents())
        .filteredOn(e -> e instanceof LecturerAvatarSet)
        .hasSize(1)
        .first()
        .extracting("lecturerId", "avatarKey")
        .containsExactly(lecturer.getId(), key);
  }
}