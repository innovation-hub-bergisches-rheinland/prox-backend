package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.project.domain.supervisor.Supervisor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProjectTest {

  @Test
  void shouldAddSupervisorOnOffer() {
    var project = createTestProject(ProjectState.PROPOSED);
    var supervisor = new Supervisor(UUID.randomUUID(), "Xavier Tester");

    project.offer(supervisor);

    assertThat(project.getSupervisors()).containsExactly(supervisor);
  }

  @Test
  void shouldNotRemoveLastSupervisor() {
    var supervisor = new Supervisor(UUID.randomUUID(), "Xavier Tester");
    var project = createProjectWithSupervisors(List.of(supervisor));

    assertThrows(RuntimeException.class, () -> project.removeSupervisor(supervisor));
  }

  @Test
  void shouldRemoveSupervisor() {
    var supervisorXavier = new Supervisor(UUID.randomUUID(), "Xavier Tester");
    var supervisorHomer = new Supervisor(UUID.randomUUID(), "Homer Simpson");
    var project = createProjectWithSupervisors(List.of(supervisorHomer, supervisorXavier));

    project.removeSupervisor(supervisorHomer);

    assertThat(project.getSupervisors()).doesNotContain(supervisorHomer);
  }

  @Test
  void shouldAddSupervisor() {
    var supervisorXavier = new Supervisor(UUID.randomUUID(), "Xavier Tester");
    var supervisorHomer = new Supervisor(UUID.randomUUID(), "Homer Simpson");
    var project = createProjectWithSupervisors(List.of(supervisorXavier));

    project.addSupervisor(supervisorHomer);

    assertThat(project.getSupervisors()).contains(supervisorHomer);
  }

  private Project createProjectWithSupervisors(Collection<Supervisor> supervisors) {
    return new Project(
        UUID.randomUUID(),
        UUID.randomUUID(),
        null,
        "Test",
        "Test",
        "Test",
        "Test",
        null,
        new ProjectStatus(ProjectState.PROPOSED, Instant.now()),
        null,
        new ArrayList<>(supervisors),
        Collections.emptyList());
  }

  private Project createTestProject(ProjectState state) {
    return new Project(
        UUID.randomUUID(),
        UUID.randomUUID(),
        null,
        "Test",
        "Test",
        "Test",
        "Test",
        null,
        new ProjectStatus(state, Instant.now()),
        null,
        Collections.emptyList(),
        Collections.emptyList());
  }
}
