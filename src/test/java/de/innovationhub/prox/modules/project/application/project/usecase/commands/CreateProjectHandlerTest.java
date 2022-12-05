package de.innovationhub.prox.modules.project.application.project.usecase.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.modules.auth.contract.AuthenticationFacade;
import de.innovationhub.prox.modules.project.DisciplineFixtures;
import de.innovationhub.prox.modules.project.ModuleTypeFixtures;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectRequest;
import de.innovationhub.prox.modules.project.application.project.web.dto.CreateProjectRequest.TimeBoxDto;
import de.innovationhub.prox.modules.project.application.project.web.dto.CurriculumContextRequest;
import de.innovationhub.prox.modules.project.domain.discipline.DisciplineRepository;
import de.innovationhub.prox.modules.project.domain.module.ModuleTypeRepository;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateProjectHandlerTest {
  ProjectRepository projectRepository = mock(ProjectRepository.class);
  ModuleTypeRepository moduleTypeRepository = mock(ModuleTypeRepository.class);
  DisciplineRepository disciplineRepository = mock(DisciplineRepository.class);
  AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

  CreateProjectHandler createProjectHandler = new CreateProjectHandler(projectRepository, moduleTypeRepository, disciplineRepository, authenticationFacade);

  @Test
  void shouldCreate() {
    var userId = UUID.randomUUID();
    when(authenticationFacade.currentAuthenticatedId()).thenReturn(userId);
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
        )
    );

    createProjectHandler.handle(command);

    var captor = ArgumentCaptor.forClass(Project.class);
    verify(projectRepository).save(captor.capture());
    assertThat(captor.getValue())
        .satisfies(p -> {
          assertThat(p.getAuthor().getUserId()).isEqualTo(userId);
          assertThat(p.getTitle()).isEqualTo("Test Project");
          assertThat(p.getDescription()).isEqualTo("Test");
          assertThat(p.getSummary()).isEqualTo("Test");
          assertThat(p.getRequirement()).isEqualTo("Test");
          assertThat(p.getCurriculumContext().getModuleTypes())
              .containsExactlyInAnyOrderElementsOf(ModuleTypeFixtures.ALL);
          assertThat(p.getCurriculumContext().getDisciplines())
              .containsExactlyInAnyOrderElementsOf(DisciplineFixtures.ALL);
          assertThat(p.getTimeBox().getStartDate()).isEqualTo(command.timeBox().start());
          assertThat(p.getTimeBox().getEndDate()).isEqualTo(command.timeBox().end());
        });
  }
}