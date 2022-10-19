package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProjectTest {

  @Test
  void shouldAddSupervisorOnOffer() {
    var project = createTestProject(ProjectState.PROPOSED);
    var supervisorId = UUID.randomUUID();

    project.offer(supervisorId);

    assertThat(project.getSupervisors()).containsExactly(supervisorId);
  }

  @Test
  void shouldNotRemoveLastSupervisor() {
    var supervisorId = UUID.randomUUID();
    var project = createProjectWithSupervisors(List.of(supervisorId));

    assertThrows(RuntimeException.class, () -> project.removeSupervisor(supervisorId));
  }

  @Test
  void shouldRemoveSupervisor() {
    var supervisorId = UUID.randomUUID();
    var project = createProjectWithSupervisors(List.of(supervisorId, UUID.randomUUID()));

    project.removeSupervisor(supervisorId);

    assertThat(project.getSupervisors()).doesNotContain(supervisorId);
  }

  @Test
  void shouldAddSupervisor() {
    var supervisorId = UUID.randomUUID();
    var project = createProjectWithSupervisors(List.of(UUID.randomUUID()));

    project.addSupervisor(supervisorId);

    assertThat(project.getSupervisors()).contains(supervisorId);
  }

  private Project createProjectWithSupervisors(Collection<UUID> supervisors) {
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
        new HashSet<>(supervisors),
        Collections.emptySet());
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
        Collections.emptySet(),
        Collections.emptySet());
  }
}
