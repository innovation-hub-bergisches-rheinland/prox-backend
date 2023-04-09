package de.innovationhub.prox.modules.user.domain.profile;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

class UserProfileRepositoryIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  UserProfileRepository userProfileRepository;

  @AfterEach
  void tearDown() {
    userProfileRepository.deleteAll();
  }

  @Test
  void shouldNotIncludeNotVisibleLecturers() {
    var up = createDummyLecturer(false);
    userProfileRepository.save(up);

    var result = userProfileRepository.findAllLecturerProfiles(Pageable.unpaged());

    assertThat(result)
        .hasSize(0);
  }

  @Test
  void shouldNotIncludeNotVisibleProfiles() {
    var up = createDummyLecturer(false);
    userProfileRepository.save(up);

    var result = userProfileRepository.search(up.getDisplayName(), Pageable.unpaged());

    assertThat(result)
        .hasSize(0);
  }

  @Test
  void shouldNotIncludeNotVisibleLecturersInSearch() {
    var up = createDummyLecturer(false);
    userProfileRepository.save(up);

    var result = userProfileRepository.searchLecturers("Xavier Tester", Pageable.unpaged());

    assertThat(result)
        .hasSize(0);
  }

  @Test
  void shouldFindLecturer() {
    var up = createDummyLecturer(true);
    userProfileRepository.save(up);

    var result = userProfileRepository.searchLecturers("Xavier Tester", Pageable.unpaged());

    assertThat(result)
        .hasSize(1)
        .first()
        .satisfies(r -> assertThat(r.getId()).isEqualTo(up.getId()));
  }

  @Test
  void shouldFindProfile() {
    var up = createDummyLecturer(true);
    userProfileRepository.save(up);

    var result = userProfileRepository.search("Xavier Tester", Pageable.unpaged());

    assertThat(result)
        .hasSize(1)
        .first()
        .satisfies(r -> assertThat(r.getId()).isEqualTo(up.getId()));
  }

  @Test
  void shouldFindLecturersWithTags() {
    var up = createDummyLecturer(true);
    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());
    up.tagProfile(tags);
    userProfileRepository.save(up);

    var result = userProfileRepository.findAllLecturersWithAnyTag(List.of(tags.get(0)), Pageable.unpaged());

    assertThat(result)
        .hasSize(1);
  }

  private UserProfile createDummyUserProfile(boolean visibility) {
    return UserProfile.create(UUID.randomUUID(), "Xavier Tester", "Lorem Ipsum",
        new ContactInformation("Test", "Test", "Test"), visibility);
  }

  private UserProfile createDummyLecturer(boolean visibility) {
    var up = createDummyUserProfile(visibility);
    up.createLecturerProfile(createDummyLecturerProfileInfo());
    return up;
  }

  private LecturerProfileInformation createDummyLecturerProfileInfo() {
    return new LecturerProfileInformation();
  }
}
