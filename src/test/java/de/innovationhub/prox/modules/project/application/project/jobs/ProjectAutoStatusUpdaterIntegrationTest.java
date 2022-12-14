package de.innovationhub.prox.modules.project.application.project.jobs;


import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.AbstractIntegrationTest;
import de.innovationhub.prox.modules.project.domain.project.Author;
import de.innovationhub.prox.modules.project.domain.project.CurriculumContext;
import de.innovationhub.prox.modules.project.domain.project.Project;
import de.innovationhub.prox.modules.project.domain.project.ProjectRepository;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import de.innovationhub.prox.modules.project.domain.project.Supervisor;
import de.innovationhub.prox.modules.project.domain.project.TimeBox;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProjectAutoStatusUpdaterIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  ProjectAutoStatusUpdater projectAutoStatusUpdater;

  @Autowired
  ProjectRepository projectRepository;

  @Test
  void shouldStartProjectWithTimeBoxStartInPast() {
    var start = LocalDate.now().minus(1, DAYS);
    var end = LocalDate.now().plus(1, DAYS);
    var project = createProjectWithTimeBox(start, end);

    projectAutoStatusUpdater.run();

    var updatedProject = projectRepository.findById(project.getId());
    assertThat(updatedProject)
        .isPresent()
        .get()
        .satisfies(p -> assertThat(p.getStatus().getState()).isEqualTo(ProjectState.RUNNING));
  }

  @Test
  void shouldNotStartProjectWithTimeBoxStartInFuture() {
    var start = LocalDate.now().plus(1, DAYS);
    var end = LocalDate.now().plus(2, DAYS);
    var project = createProjectWithTimeBox(start, end);

    projectAutoStatusUpdater.run();

    var updatedProject = projectRepository.findById(project.getId());
    assertThat(updatedProject)
        .isPresent()
        .get()
        .satisfies(p -> assertThat(p.getStatus().getState()).isEqualTo(ProjectState.OFFERED));
  }

  @Test
  void shouldEndProjectWithTimeBoxEndInPast() {
    var start = LocalDate.now().minus(2, DAYS);
    var end = LocalDate.now().minus(1, DAYS);
    var project = createProjectWithTimeBox(start, end);
    project.start();
    projectRepository.save(project);

    projectAutoStatusUpdater.run();

    var updatedProject = projectRepository.findById(project.getId());
    assertThat(updatedProject)
        .isPresent()
        .get()
        .satisfies(p -> assertThat(p.getStatus().getState()).isEqualTo(ProjectState.COMPLETED));
  }

  @Test
  void shouldNotEndProjectWithTimeBoxStartInFuture() {
    var start = LocalDate.now().minus(1, DAYS);
    var end = LocalDate.now().plus(2, DAYS);
    var project = createProjectWithTimeBox(start, end);

    projectAutoStatusUpdater.run();

    var updatedProject = projectRepository.findById(project.getId());
    assertThat(updatedProject)
        .isPresent()
        .get()
        .satisfies(p -> assertThat(p.getStatus().getState()).isEqualTo(ProjectState.RUNNING));
  }

  private Project createProjectWithTimeBox(LocalDate start, LocalDate end) {
    var project = Project.create(
        new Author(UUID.randomUUID()),
        "Test Project",
        "Test Description",
        "Test Location",
        "Test",
        new CurriculumContext(List.of(), List.of()),
        new TimeBox(start, end)
    );
    project.offer(new Supervisor(UUID.randomUUID()));
    return projectRepository.save(project);
  }
}