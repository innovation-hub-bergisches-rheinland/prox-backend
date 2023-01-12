package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.innovationhub.prox.modules.project.domain.project.events.ProjectCreated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectInterestStated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectInterestUnstated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectOffered;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectStateUpdated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import de.innovationhub.prox.modules.project.domain.project.exception.ProjectStateException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProjectTest {

  @Test
  void shouldRegisterCreateEvent() {
    var project = Project.create(new Author(UUID.randomUUID()), "Test Project", "Test",
        "Test", "Test", new CurriculumContext(), null, null, List.of());
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
  void shouldThrowWhenProjectNotProposed() {
    var project = createTestProject(ProjectState.OFFERED);
    var supervisor = UUID.randomUUID();

    assertThatThrownBy(() -> project.applyCommitment(supervisor))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void shouldThrowWhenProjectHasSupervisors() {
    var project = createTestProject(ProjectState.OFFERED);
    project.setSupervisors(List.of(UUID.randomUUID()));
    var supervisor = UUID.randomUUID();

    assertThatThrownBy(() -> project.applyCommitment(supervisor))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void shouldAddSupervisorOnOffer() {
    var project = createTestProject(ProjectState.PROPOSED);
    var supervisor = UUID.randomUUID();

    project.applyCommitment(supervisor);

    assertThat(project.getSupervisors())
        .extracting(Supervisor::getLecturerId)
        .containsExactly(supervisor);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectOffered)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectOffered.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
          assertThat(event.supervisor())
              .extracting(Supervisor::getLecturerId)
              .containsExactlyInAnyOrder(supervisor);
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

  @Test
  void shouldRegisterInterest() {
    var project = createTestProject(ProjectState.PROPOSED);
    var user = new InterestedUser(UUID.randomUUID());

    project.stateInterest(user);

    assertThat(project.getInterestedUsers()).containsExactly(user);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectInterestStated)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectInterestStated.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
          assertThat(event.user()).isEqualTo(user);
        });
  }

  @Test
  void shouldRemoveInterest() {
    var project = createTestProject(ProjectState.PROPOSED);
    var user = new InterestedUser(UUID.randomUUID());
    project.stateInterest(user);

    project.unstateInterest(user);

    assertThat(project.getInterestedUsers()).doesNotContain(user);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectInterestUnstated)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectInterestUnstated.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
          assertThat(event.user()).isEqualTo(user);
        });
  }

  @Test
  void shouldThrowWhenProjectIsCompleted() {
    var project = createTestProject(ProjectState.COMPLETED);

    assertThatThrownBy(() -> project.stateInterest(new InterestedUser(UUID.randomUUID())))
        .isInstanceOf(ProjectStateException.class);
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
        null,
        new HashSet<>());
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
        null,
        new HashSet<>());
  }
}
