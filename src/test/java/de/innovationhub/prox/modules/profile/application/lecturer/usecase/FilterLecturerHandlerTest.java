package de.innovationhub.prox.modules.profile.application.lecturer.usecase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import org.junit.jupiter.api.Test;

class FilterLecturerHandlerTest {
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);
  FilterLecturerHandler handler = new FilterLecturerHandler(lecturerRepository);

  @Test
  void shouldCallRepository() {
    var query = "lol";

    handler.handle(query);

    verify(lecturerRepository).filter(query);
  }
}