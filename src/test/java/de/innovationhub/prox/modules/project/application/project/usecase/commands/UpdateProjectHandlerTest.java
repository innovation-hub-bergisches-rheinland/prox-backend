package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.project.DisciplineFixtures;
import de.innovationhub.prox.modules.project.ModuleTypeFixtures;
import de.innovationhub.prox.modules.project.ProjectFixtures;
import de.innovationhub.prox.modules.project.application.project.dto.CreateProjectRequest;
import de.innovationhub.prox.modules.project.application.project.dto.CreateProjectRequest.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.dto.CurriculumContextRequest;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateProjectHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  ModuleTypeRepository moduleTypeRepository = mock(ModuleTypeRepository.class);
  DisciplineRepository disciplineRepository = mock(DisciplineRepository.class);

  UpdateProjectHandler createProjectHandler = new UpdateProjectHandler(projectRepository, moduleTypeRepository, disciplineRepository);

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
    when(moduleTypeRepository.findByKeyIn(any())).thenReturn(ModuleTypeFixtures.ALL);
    when(disciplineRepository.findByKeyIn(any())).thenReturn(DisciplineFixtures.ALL);

    var command = new CreateProjectRequest(
        "Test Project",
        "Test",
        "Test",
        "Test",
        new CurriculumContextRequest(
            List.of("BA"),
            List.of("ING", "INF")
        ),
        new TimeBoxDto(
            LocalDate.now(),
            LocalDate.now()
        ),
        UUID.randomUUID(),
        Set.of(UUID.randomUUID())
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
              .containsExactlyInAnyOrderElementsOf(ModuleTypeFixtures.ALL.stream().map(
                  ModuleType::getKey).toList());
          assertThat(p.getCurriculumContext().getDisciplines())
              .containsExactlyInAnyOrderElementsOf(
                  DisciplineFixtures.ALL.stream().map(Discipline::getKey).toList());
          assertThat(p.getTimeBox().getStartDate()).isEqualTo(command.timeBox().start());
          assertThat(p.getTimeBox().getEndDate()).isEqualTo(command.timeBox().end());
          assertThat(p.getPartner().getOrganizationId()).isEqualTo(command.partnerId());
          assertThat(p.getSupervisors()).hasSize(1).extracting(Supervisor::getLecturerId)
              .containsExactlyInAnyOrderElementsOf(command.supervisors());
        });
  }
}