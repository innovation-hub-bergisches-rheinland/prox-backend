package de.innovationhub.prox.modules.project.application.project.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.project.DisciplineFixtures;
import de.innovationhub.prox.modules.project.ModuleTypeFixtures;
import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.web.dto.CurriculumContextDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.PartnerDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.SupervisorDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.UpdateProjectDto;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateProjectHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  ModuleTypeRepository moduleTypeRepository = mock(ModuleTypeRepository.class);
  DisciplineRepository disciplineRepository = mock(DisciplineRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

  UpdateProjectHandler createProjectHandler = new UpdateProjectHandler(projectRepository, moduleTypeRepository, disciplineRepository, authenticationFacade);

  @Test
  void shouldThrowOnNotFound() {
    when(projectRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> createProjectHandler.handle(UUID.randomUUID(), null))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldUpdate() {
    var project = ProjectFixtures.build_a_project();
    when(projectRepository.findById(any())).thenReturn(Optional.of(project));
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(ProjectFixtures.A_PROJECT_CREATOR_ID);
    when(moduleTypeRepository.findByKeyIn(any())).thenReturn(ModuleTypeFixtures.ALL);
    when(disciplineRepository.findByKeyIn(any())).thenReturn(DisciplineFixtures.ALL);

    var command = new UpdateProjectDto(
        "Test Project",
        "Test",
        "Test",
        "Test",
        new CurriculumContextDto(
            List.of("BA"),
            List.of("ING", "INF")
        ),
        new PartnerDto(UUID.randomUUID()),
        new TimeBoxDto(
            LocalDate.now(),
            LocalDate.now()
        ),
        List.of(new SupervisorDto(UUID.randomUUID()))
    );

    createProjectHandler.handle(project.getId(), command);

    var captor = ArgumentCaptor.forClass(Project.class);
    verify(projectRepository).save(captor.capture());

    assertThat(captor.getValue())
        .satisfies(p -> {
          assertThat(p.getAuthor().getUserId()).isEqualTo(project.getAuthor().getUserId());
          assertThat(p.getTitle()).isEqualTo("Test Project");
          assertThat(p.getDescription()).isEqualTo("Test");
          assertThat(p.getSummary()).isEqualTo("Test");
          assertThat(p.getRequirement()).isEqualTo("Test");
          assertThat(p.getCurriculumContext().getModuleTypes())
              .containsExactlyInAnyOrderElementsOf(ModuleTypeFixtures.ALL);
          assertThat(p.getCurriculumContext().getDisciplines())
              .containsExactlyInAnyOrderElementsOf(DisciplineFixtures.ALL);
          assertThat(p.getPartner().getOrganizationId()).isEqualTo(command.partner().organizationId());
          assertThat(p.getTimeBox().getStartDate()).isEqualTo(command.timeboxDto().start());
          assertThat(p.getTimeBox().getEndDate()).isEqualTo(command.timeboxDto().end());
        });
  }
}