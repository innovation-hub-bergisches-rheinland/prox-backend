package de.innovationhub.prox.project.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.project.project.exception.InvalidProjectStateTransitionException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProjectTest {

  private static Stream<Arguments> validStatusTransitions() {
    return Stream.of(
      Arguments.of(ProjectState.ARCHIVED, (Consumer<Project>) Project::unarchive, ProjectState.PROPOSED),
      Arguments.of(ProjectState.PROPOSED, (Consumer<Project>) Project::archive, ProjectState.ARCHIVED),
      Arguments.of(ProjectState.PROPOSED, (Consumer<Project>) project -> project.offer(UUID.randomUUID()), ProjectState.OFFERED),
      Arguments.of(ProjectState.ARCHIVED, (Consumer<Project>) Project::stale, ProjectState.STALE),
      Arguments.of(ProjectState.OFFERED, (Consumer<Project>) Project::start, ProjectState.RUNNING),
      Arguments.of(ProjectState.RUNNING, (Consumer<Project>) Project::complete, ProjectState.COMPLETED)
    );
  }

  private static Stream<Arguments> invalidStatusTransitions() {
    // We're going to build invalid state transitions from the valid ones
    return validStatusTransitions()
      .flatMap(arguments ->
        Arrays.stream(ProjectState.values())
          .filter(s -> s != arguments.get()[0])
          .map(s -> Arguments.of(s, arguments.get()[1]))
      );
  }

  @ParameterizedTest
  @MethodSource("validStatusTransitions")
  void shouldUpdateStates(ProjectState givenStatus, Consumer<Project> statusTransition, ProjectState expectedStatus) {
    var project = createTestProject(givenStatus);

    statusTransition.accept(project);

    assertThat(project.getStatus().getState())
      .isEqualTo(expectedStatus);
  }

  @ParameterizedTest
  @MethodSource("invalidStatusTransitions")
  void shouldThrowOnInvalidStateTransition(ProjectState givenStatus, Consumer<Project> statusTransition) {
    var project = createTestProject(givenStatus);

    assertThrows(InvalidProjectStateTransitionException.class, () -> statusTransition.accept(project));
    assertThat(project.getStatus().getState())
      .isEqualTo(givenStatus);
  }

  @Test
  void shouldAddSupervisorOnOffer() {
    var project = createTestProject(ProjectState.PROPOSED);
    var supervisorId = UUID.randomUUID();

    project.offer(supervisorId);

    assertThat(project.getSupervisors())
      .containsExactly(supervisorId);
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

    assertThat(project.getSupervisors())
      .doesNotContain(supervisorId);
  }

  @Test
  void shouldAddSupervisor() {
    var supervisorId = UUID.randomUUID();
    var project = createProjectWithSupervisors(List.of(UUID.randomUUID()));

    project.addSupervisor(supervisorId);

    assertThat(project.getSupervisors())
      .contains(supervisorId);
  }

  private Project createProjectWithSupervisors(Collection<UUID> supervisors) {
    return new Project(UUID.randomUUID(),
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
      Collections.emptySet()
    );
  }

  private Project createTestProject(ProjectState state) {
    return new Project(UUID.randomUUID(),
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
      Collections.emptySet()
      );
  }
}

