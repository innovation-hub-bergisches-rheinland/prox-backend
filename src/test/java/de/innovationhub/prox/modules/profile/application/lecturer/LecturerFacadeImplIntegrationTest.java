package de.innovationhub.prox.modules.profile.application.lecturer;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LecturerFacadeImplIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  LecturerFacadeImpl lecturerFacade;

  @Autowired
  LecturerRepository lecturerRepository;

  private Lecturer createDummyLecturer() {
    return Lecturer.create(UUID.randomUUID(), "Max Mustermann");
  }

  @Test
  void shouldFindLecturer() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    var result = lecturerFacade.get(lecturer.getId());
    assertThat(result)
        .isPresent()
        .get()
        .satisfies(l -> assertThat(l.id()).isEqualTo(lecturer.getId()))
        .satisfies(l -> assertThat(l.name()).isEqualTo(lecturer.getName()));
  }
}