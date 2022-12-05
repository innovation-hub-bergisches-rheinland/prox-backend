package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProjectStatusTest {

  private static Stream<Arguments> validStatusTransitions() {
    return Stream.of(
        Arguments.of(ProjectState.PROPOSED, ProjectState.ARCHIVED),
        Arguments.of(ProjectState.PROPOSED, ProjectState.OFFERED),
        Arguments.of(ProjectState.ARCHIVED, ProjectState.PROPOSED),
        Arguments.of(ProjectState.ARCHIVED, ProjectState.STALE),
        Arguments.of(ProjectState.OFFERED, ProjectState.RUNNING),
        Arguments.of(ProjectState.OFFERED, ProjectState.COMPLETED),
        Arguments.of(ProjectState.RUNNING, ProjectState.OFFERED),
        Arguments.of(ProjectState.RUNNING, ProjectState.COMPLETED),
        Arguments.of(ProjectState.COMPLETED, ProjectState.OFFERED),
        Arguments.of(ProjectState.COMPLETED, ProjectState.RUNNING)
        );
  }

  @ParameterizedTest(name = "should update state from {0} to {1}")
  @MethodSource("validStatusTransitions")
  void shouldUpdateStates(ProjectState givenState, ProjectState expectedState) {
    var status = new ProjectStatus(givenState, Instant.now());

    status.updateState(expectedState);

    assertThat(status.getState()).isEqualTo(expectedState);
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
