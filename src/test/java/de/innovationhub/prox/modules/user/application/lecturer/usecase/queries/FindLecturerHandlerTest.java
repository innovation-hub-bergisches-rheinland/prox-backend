package de.innovationhub.prox.modules.user.application.lecturer.usecase.queries;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.innovationhub.prox.modules.user.domain.lecturer.LecturerRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FindLecturerHandlerTest {
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);
  FindLecturerHandler handler = new FindLecturerHandler(lecturerRepository);

  @Test
  void shouldThrowOnNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null));
  }

  @Test
  void shouldCallRepository() {
    var id = UUID.randomUUID();

    handler.handle(id);

    verify(lecturerRepository).findById(id);
  }
}