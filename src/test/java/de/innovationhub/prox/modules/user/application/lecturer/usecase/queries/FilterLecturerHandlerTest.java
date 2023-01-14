package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.user.domain.lecturer.LecturerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

class FilterLecturerHandlerTest {
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);
  FilterLecturerHandler handler = new FilterLecturerHandler(lecturerRepository);

  @Test
  void shouldCallRepository() {
    var query = "lol";

    handler.handle(query, Pageable.unpaged());

    verify(lecturerRepository).filter(query, Pageable.unpaged());
  }
}