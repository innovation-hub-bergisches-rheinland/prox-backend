package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.project.domain.project.exception.InvalidProjectStateTransitionException;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProjectStatusTest {

  private static Stream<Arguments> validStatusTransitions() {
    return ProjectState.TRANSITIONS.entrySet()
        .stream()
        .flatMap(
            t -> t.getValue().stream()
                .map(s -> Arguments.of(t.getKey(), s)));
  }

  private static Stream<Arguments> invalidStatusTransitions() {
    // We're going to build invalid state transitions from the valid ones
    return validStatusTransitions()
        .flatMap(
            arguments ->
                Arrays.stream(ProjectState.values())
                    .filter(s -> s != arguments.get()[0])
                    .map(s -> Arguments.of(s, arguments.get()[1])));
  }

  @ParameterizedTest(name = "should update state from {0} to {1}")
  @MethodSource("validStatusTransitions")
  void shouldUpdateStates(ProjectState givenState, ProjectState expectedState) {
    var status = new ProjectStatus(givenState, Instant.now());

    status.updateState(expectedState);

    assertThat(status.getState()).isEqualTo(expectedState);
  }

  @ParameterizedTest(name = "should not update state from {0} to {1}")
  @MethodSource("invalidStatusTransitions")
  void shouldThrowOnInvalidStateTransition(
      ProjectState givenState, ProjectState invalidState) {
    var status = new ProjectStatus(givenState, Instant.now());

    assertThrows(
        InvalidProjectStateTransitionException.class, () -> status.updateState(invalidState));
    assertThat(status.getState()).isEqualTo(givenState);
  }

  @Test
  void shouldAllowTransitionToItself() {
    var status = new ProjectStatus(ProjectState.OFFERED, Instant.now());
    status.updateState(ProjectState.OFFERED);

    assertThat(status.getState()).isEqualTo(ProjectState.OFFERED);
  }

  @Test
  void shouldUpdateTimestamp() {
    var currentMoment = Instant.now();
    var status = new ProjectStatus(ProjectState.OFFERED, currentMoment);

    status.updateState(ProjectState.RUNNING);

    assertThat(status.getUpdatedAt()).isNotSameAs(currentMoment).isAfterOrEqualTo(currentMoment);
  }
}
