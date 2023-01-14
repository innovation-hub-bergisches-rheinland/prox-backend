package de.innovationhub.prox.modules.user.application.lecturer.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.infra.storage.StorageProvider;
import de.innovationhub.prox.modules.user.contract.user.AuthenticationFacade;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfile;
import de.innovationhub.prox.modules.user.domain.lecturer.LecturerProfileRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SetLecturerAvatarHandlerTestProfile {

  StorageProvider storageProvider = mock(StorageProvider.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
  LecturerProfileRepository lecturerRepository = mock(LecturerProfileRepository.class);
  SetLecturerAvatarHandler handler = new SetLecturerAvatarHandler(storageProvider,
      authenticationFacade, lecturerRepository);

  private LecturerProfile createDummyLecturer() {
    return LecturerProfile.create(UUID.randomUUID(), "Max Mustermann");
  }

  @Test
  void shouldThrowWhenLecturerNotFound() {
    when(lecturerRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), new byte[]{}, ""));
  }

  @Test
  void shouldSetLecturerAvatar() throws IOException {
    var lecturer = createDummyLecturer();
    when(lecturerRepository.findById(any())).thenReturn(Optional.of(lecturer));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(lecturer.getUserId());

    var content = new byte[] { 1, 2, 3 };
    var contentType = "image/test";
    handler.handle(UUID.randomUUID(), content, contentType);

    var fileIdCapture = ArgumentCaptor.forClass(String.class);
    verify(storageProvider).storeFile(fileIdCapture.capture(), aryEq(content), eq(contentType));

    var lecturerCapture = ArgumentCaptor.forClass(LecturerProfile.class);
    verify(lecturerRepository).save(lecturerCapture.capture());

    assertThat(lecturerCapture.getValue())
        .satisfies(l -> assertThat(l.getAvatarKey()).isEqualTo(fileIdCapture.getValue()));
  }
}