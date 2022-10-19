package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.modules.project.domain.project.exception.InvalidProjectStateTransitionException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProjectStatus {

  @Enumerated(EnumType.STRING)
  private ProjectState state;
  private Instant updatedAt;

  /**
   * Updates the state of the project.
   *
   * @param state new state
   * @throws InvalidProjectStateTransitionException if the transition is not allowed
   */
  void updateState(ProjectState state) {
    Objects.requireNonNull(state);
    if (ProjectState.TRANSITIONS.getOrDefault(this.state, List.of()).contains(state)) {
      this.state = state;
      this.updatedAt = Instant.now();
    } else {
      throw new InvalidProjectStateTransitionException(
          "Cannot transition from " + this.state + " to " + state);
    }
  }
}
