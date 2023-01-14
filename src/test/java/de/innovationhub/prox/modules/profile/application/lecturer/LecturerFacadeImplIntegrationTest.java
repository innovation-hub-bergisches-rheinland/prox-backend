package de.innovationhub.prox.modules.profile.application.lecturer;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LecturerFacadeImplIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  LecturerFacadeImpl lecturerFacade;

  @Autowired
  LecturerRepository lecturerRepository;

  private Lecturer createDummyLecturer() {
    var lecturer = Lecturer.create(UUID.randomUUID(), "Max Mustermann");
    lecturer.setVisibleInPublicSearch(true);
    return lecturer;
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
        .satisfies(l -> assertThat(l.displayName()).isEqualTo(lecturer.getDisplayName()));
  }

  @Test
  void shouldFindLecturerList() {
    var lecturer = createDummyLecturer();
    lecturerRepository.save(lecturer);

    var result = lecturerFacade.findByIds(List.of(lecturer.getId()));
    assertThat(result)
        .hasSize(1)
        .first()
        .satisfies(l -> assertThat(l.id()).isEqualTo(lecturer.getId()))
        .satisfies(l -> assertThat(l.displayName()).isEqualTo(lecturer.getDisplayName()));
  }
}