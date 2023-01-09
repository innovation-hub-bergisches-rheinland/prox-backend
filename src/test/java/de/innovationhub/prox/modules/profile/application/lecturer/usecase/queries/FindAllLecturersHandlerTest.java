package de.innovationhub.prox.modules.profile.application.lecturer.usecase.queries;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

class FindAllLecturersHandlerTest {
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);
  FindAllLecturersHandler handler = new FindAllLecturersHandler(lecturerRepository);

  @Test
  void shouldCallRepository() {
    handler.handle(Pageable.unpaged());

    verify(lecturerRepository).findAll(Pageable.unpaged());
  }
}