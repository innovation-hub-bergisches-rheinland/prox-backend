package de.innovationhub.prox.modules.profile.domain.lecturer;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class LecturerRepositoryIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  LecturerRepository lecturerRepository;

  @Test
  @SuppressWarnings("java:S5841")
  void shouldNotIncludeInvisibleProfilesInFindAll() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisibleInPublicSearch(false);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.findAll();

    //NOSONAR
    assertThat(result)
        .doesNotContain(lecturer);
  }

  @Test
  @SuppressWarnings("java:S5841")
  void shouldNotIncludeInvisibleProfilesInFilter() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisibleInPublicSearch(false);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.filter("Max Mustermann");

    assertThat(result)
        .doesNotContain(lecturer);
  }

  @Test
  @SuppressWarnings("java:S5841")
  void shouldIncludeInvisibleProfilesInFindById() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisibleInPublicSearch(false);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.findById(lecturer.getId());

    assertThat(result)
        .isPresent()
        .get()
        .isEqualTo(lecturer);
  }

  @Test
  void shouldFilterPartial() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisibleInPublicSearch(true);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.filter("Max");

    assertThat(result).contains(lecturer);
  }

  @Test
  void shouldFilterCaseInsensitive() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisibleInPublicSearch(true);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.filter("max mustermann");

    assertThat(result).contains(lecturer);
  }
}