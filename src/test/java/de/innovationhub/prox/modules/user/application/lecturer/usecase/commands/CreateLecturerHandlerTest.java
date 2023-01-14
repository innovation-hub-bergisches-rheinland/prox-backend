package de.innovationhub.prox.modules.user.application.lecturer.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.user.contract.account.ProxUserAccountView;
import de.innovationhub.prox.modules.user.contract.account.UserAccountFacade;
import de.innovationhub.prox.modules.user.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateLecturerHandlerTest {

  UserAccountFacade userAccountFacade = mock(UserAccountFacade.class);
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);

  CreateLecturerHandler handler = new CreateLecturerHandler(lecturerRepository, userAccountFacade);

  @Test
  void shouldThrowOnNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null));
  }

  @Test
  void shouldCreateLecturer() {
    var userId = UUID.randomUUID();
    var user = new ProxUserAccountView(userId, "Xavier Tester", "xavier.tester@example.com");
    when(userAccountFacade.findById(userId)).thenReturn(Optional.of(user));

    handler.handle(userId);

    var captor = ArgumentCaptor.forClass(Lecturer.class);
    verify(lecturerRepository).save(captor.capture());
    var lecturer = captor.getValue();

    assertThat(lecturer.getUserId()).isEqualTo(userId);
    assertThat(lecturer.getDisplayName()).isEqualTo("Xavier Tester");
    assertThat(lecturer.getProfile().getEmail()).isEqualTo("xavier.tester@example.com");
    assertThat(lecturer.getVisibleInPublicSearch()).isEqualTo(false);
  }
}