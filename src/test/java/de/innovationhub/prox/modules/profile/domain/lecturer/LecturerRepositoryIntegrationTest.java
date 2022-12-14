package de.innovationhub.prox.modules.profile.domain.lecturer;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class LecturerRepositoryIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  LecturerRepository lecturerRepository;

  @Test
  void shouldNotIncludeInvisibleProfilesInFindAll() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisible(false);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.findAll();

    assertThat(result).isEmpty();
  }

  @Test
  void shouldNotIncludeInvisibleProfilesInFilter() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisible(false);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.filter("Max Mustermann");

    assertThat(result).isEmpty();
  }

  @Test
  void shouldFilterPartial() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisible(true);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.filter("Max");

    assertThat(result).contains(lecturer);
  }

  @Test
  void shouldFilterCaseInsensitive() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisible(true);
    lecturerRepository.save(lecturer);

    var result = lecturerRepository.filter("max mustermann");

    assertThat(result).contains(lecturer);
  }
}