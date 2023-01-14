package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

class FindAllLecturersHandlerTest {

  LecturerProfileRepository lecturerRepository = mock(LecturerProfileRepository.class);
  FindAllLecturersHandler handler = new FindAllLecturersHandler(lecturerRepository);

  @Test
  void shouldCallRepository() {
    handler.handle(Pageable.unpaged());

    verify(lecturerRepository).findAll(Pageable.unpaged());
  }
}