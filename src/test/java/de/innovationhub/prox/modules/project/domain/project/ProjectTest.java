package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.modules.project.domain.project.events.ProjectCreated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectOffered;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectStateUpdated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProjectTest {

  @Test
  void shouldRegisterCreateEvent() {
    var project = Project.create(new Author(UUID.randomUUID()), "Test Project", "Test",
        "Test", "Test", new CurriculumContext(), null, null);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectCreated)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectCreated.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
        });
  }

  @Test
  void shouldSetState() {
    var project = createTestProject(ProjectState.PROPOSED);

    project.updateState(ProjectState.OFFERED);

    assertThat(project.getStatus().getState())
        .isEqualTo(ProjectState.OFFERED);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectStateUpdated)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectStateUpdated.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
          assertThat(event.state()).isEqualTo(ProjectState.OFFERED);
        });
  }

  @Test
  void shouldAddSupervisorOnOffer() {
    var project = createTestProject(ProjectState.PROPOSED);
    var supervisor = new Supervisor(UUID.randomUUID());

    project.offer(supervisor);

    assertThat(project.getSupervisors()).containsExactly(supervisor);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectOffered)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectOffered.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
          assertThat(event.supervisor()).containsExactlyInAnyOrder(supervisor);
        });
  }

  @Test
  void shouldTagProject() {
    var project = createTestProject(ProjectState.PROPOSED);
    var tags = List.of(UUID.randomUUID(), UUID.randomUUID());

    project.setTags(tags);

    assertThat(project.getTags()).containsExactlyInAnyOrderElementsOf(tags);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectTagged)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectTagged.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
          assertThat(event.tags()).containsExactlyInAnyOrderElementsOf(tags);
        });
  }

  private Project createProjectWithSupervisors(Collection<Supervisor> supervisors) {
    return new Project(
        UUID.randomUUID(),
        new Author(UUID.randomUUID()),
        null,
        "Test",
        "Test",
        "Test",
        "Test",
        null,
        new ProjectStatus(ProjectState.PROPOSED, Instant.now()),
        null,
        new ArrayList<>(supervisors),
        null);
  }

  private Project createTestProject(ProjectState state) {
    return new Project(
        UUID.randomUUID(),
        new Author(UUID.randomUUID()),
        null,
        "Test",
        "Test",
        "Test",
        "Test",
        null,
        new ProjectStatus(state, Instant.now()),
        null,
        Collections.emptyList(),
        null);
  }
}
