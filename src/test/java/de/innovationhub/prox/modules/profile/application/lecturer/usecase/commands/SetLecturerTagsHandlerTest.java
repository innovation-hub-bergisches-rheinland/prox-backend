package de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.contract.account.AuthenticationFacade;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetLecturerTagsHandlerTest {
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);
  AuthenticationFacade authentication = mock(AuthenticationFacade.class);
  SetLecturerTagsHandler handler = new SetLecturerTagsHandler(lecturerRepository, authentication);

  private Lecturer createDummyLecturer() {
    return Lecturer.create(UUID.randomUUID(), "Max Mustermann");
  }

  @Test
  void shouldThrowWhenLecturerNotFound() {
    when(lecturerRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), List.of()));
  }

  @Test
  void shouldSetTags() {
    var lecturer = createDummyLecturer();
    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());
    when(lecturerRepository.findById(any())).thenReturn(Optional.of(lecturer));
    when(authentication.currentAuthenticatedId()).thenReturn(lecturer.getUserId());

    handler.handle(lecturer.getId(), tags);

    var lecturerArgumentCaptor = ArgumentCaptor.forClass(Lecturer.class);
    verify(lecturerRepository).save(lecturerArgumentCaptor.capture());
    assertThat(lecturerArgumentCaptor.getValue().getTags()).containsExactlyInAnyOrderElementsOf(tags);
  }
}