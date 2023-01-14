package de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.Lecturer;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.contract.account.AuthenticationFacade;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateLecturerProfileInformationHandlerTest {

  LecturerRepository lecturerRepository = mock(LecturerRepository.class);
  AuthenticationFacade authentication = mock(AuthenticationFacade.class);
  UpdateLecturerProfileHandler handler = new UpdateLecturerProfileHandler(lecturerRepository,
      authentication);

  private Lecturer createDummyLecturer() {
    return Lecturer.create(UUID.randomUUID(), "Max Mustermann");
  }

  private CreateLecturerRequestDto createDummyDto() {
    return new CreateLecturerRequestDto("Max Mustermann", new CreateLecturerProfileDto(
        "affiliation",
        "subject",
        "vita",
        List.of("publications"),
        "room",
        "consulationHour",
        "email",
        "telephone",
        "homepage",
        "collegePage"
    ), true);
  }

  @Test
  void shouldThrowWhenLecturerNotFound() {
    when(lecturerRepository.findById(any())).thenReturn(Optional.empty());
    var dto = createDummyDto();

    assertThrows(RuntimeException.class, () -> handler.handle(UUID.randomUUID(), dto));
  }

  @Test
  void shouldSetProfile() {
    var lecturer = createDummyLecturer();
    var dto = createDummyDto();
    when(lecturerRepository.findById(any())).thenReturn(Optional.of(lecturer));
    when(authentication.currentAuthenticatedId()).thenReturn(lecturer.getUserId());

    handler.handle(lecturer.getId(), dto);

    var lecturerArgumentCaptor = ArgumentCaptor.forClass(Lecturer.class);
    verify(lecturerRepository).save(lecturerArgumentCaptor.capture());
    assertThat(lecturerArgumentCaptor.getValue().getDisplayName()).isEqualTo(dto.name());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getAffiliation()).isEqualTo(
        dto.profile().affiliation());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getSubject()).isEqualTo(
        dto.profile().subject());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getVita()).isEqualTo(
        dto.profile().vita());
    assertThat(lecturerArgumentCaptor.getValue().getProfile()
        .getPublications()).containsExactlyInAnyOrderElementsOf(dto.profile().publications());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getRoom()).isEqualTo(
        dto.profile().room());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getConsultationHour()).isEqualTo(
        dto.profile().consultationHour());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getEmail()).isEqualTo(
        dto.profile().email());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getTelephone()).isEqualTo(
        dto.profile().telephone());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getHomepage()).isEqualTo(
        dto.profile().homepage());
    assertThat(lecturerArgumentCaptor.getValue().getProfile().getCollegePage()).isEqualTo(
        dto.profile().collegePage());
  }
}