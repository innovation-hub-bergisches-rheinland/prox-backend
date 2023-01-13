package de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.contract.user.ProxUserView;
import de.innovationhub.prox.modules.user.contract.user.UserFacade;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateLecturerHandlerTest {

  UserFacade userFacade = mock(UserFacade.class);
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);

  CreateLecturerHandler handler = new CreateLecturerHandler(lecturerRepository, userFacade);

  @Test
  void shouldThrowOnNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null));
  }

  @Test
  void shouldCreateLecturer() {
    var userId = UUID.randomUUID();
    var user = new ProxUserView(userId, "Xavier Tester", "xavier.tester@example.com");
    when(userFacade.findById(userId)).thenReturn(Optional.of(user));

    handler.handle(userId);

    var captor = ArgumentCaptor.forClass(Lecturer.class);
    verify(lecturerRepository).save(captor.capture());
    var lecturer = captor.getValue();

    assertThat(lecturer.getUserId()).isEqualTo(userId);
    assertThat(lecturer.getName()).isEqualTo("Xavier Tester");
    assertThat(lecturer.getProfile().getEmail()).isEqualTo("xavier.tester@example.com");
    assertThat(lecturer.getVisibleInPublicSearch()).isEqualTo(false);
  }
}