package de.innovationhub.prox.modules.profile.application.lecturer.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto;
import de.innovationhub.prox.modules.profile.application.lecturer.web.dto.CreateLecturerRequestDto.CreateLecturerProfileDto;
import de.innovationhub.prox.modules.profile.domain.lecturer.LecturerRepository;
import de.innovationhub.prox.modules.user.contract.AuthenticationFacade;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateLecturerHandlerTest {
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
  LecturerRepository lecturerRepository = mock(LecturerRepository.class);

  CreateLecturerHandler handler = new CreateLecturerHandler(lecturerRepository, authenticationFacade);
  UUID authenticatedUserId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(authenticatedUserId);
  }

  @Test
  void shouldThrowOnNull() {
    assertThrows(NullPointerException.class, () -> handler.handle(null));
  }

  @Test
  void shouldCreateLecturer() {
    var request = new CreateLecturerRequestDto(
        "Max Mustermann",
        new CreateLecturerProfileDto(
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
        ), true
    );

    when(lecturerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var lecturer = handler.handle(request);

    assertThat(lecturer.getUserId()).isEqualTo(authenticatedUserId);
    assertThat(lecturer.getName()).isEqualTo("Max Mustermann");
    assertThat(lecturer.getProfile().getAffiliation()).isEqualTo("affiliation");
    assertThat(lecturer.getProfile().getSubject()).isEqualTo("subject");
    assertThat(lecturer.getProfile().getVita()).isEqualTo("vita");
    assertThat(lecturer.getProfile().getPublications()).containsExactly("publications");
    assertThat(lecturer.getProfile().getRoom()).isEqualTo("room");
    assertThat(lecturer.getProfile().getConsultationHour()).isEqualTo("consulationHour");
    assertThat(lecturer.getProfile().getEmail()).isEqualTo("email");
    assertThat(lecturer.getProfile().getTelephone()).isEqualTo("telephone");
    assertThat(lecturer.getProfile().getHomepage()).isEqualTo("homepage");
    assertThat(lecturer.getProfile().getCollegePage()).isEqualTo("collegePage");
  }
}