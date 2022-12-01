package de.innovationhub.prox.modules.project.domain.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.innovationhub.prox.modules.project.domain.project.events.ProjectArchived;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectCompleted;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectCreated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectMarkedAsStale;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectOffered;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectPartnered;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectStarted;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectUnarchived;
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
        "Test", "Test", new CurriculumContext(), null);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectCreated)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectCreated.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
        });
  }

  @Test
  void shouldArchive() {
    var project = createTestProject(ProjectState.PROPOSED);

    project.archive();

    assertThat(project.getStatus().getState())
        .isEqualTo(ProjectState.ARCHIVED);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectArchived)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectArchived.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
        });
  }

  @Test
  void shouldPartner() {
    var project = createTestProject(ProjectState.PROPOSED);
    var organizationId = UUID.randomUUID();

    project.setPartner(new Partner(organizationId));

    assertThat(project.getPartner().getOrganizationId())
        .isEqualTo(organizationId);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectPartnered)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectPartnered.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
          assertThat(event.organizationId()).isEqualTo(organizationId);
        });
  }

  @Test
  void shouldUnarchive() {
    var project = createTestProject(ProjectState.ARCHIVED);

    project.unarchive();

    assertThat(project.getStatus().getState())
        .isEqualTo(ProjectState.PROPOSED);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectUnarchived)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectUnarchived.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
        });
  }

  @Test
  void shouldStale() {
    var project = createTestProject(ProjectState.ARCHIVED);

    project.stale();

    assertThat(project.getStatus().getState())
        .isEqualTo(ProjectState.STALE);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectMarkedAsStale)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectMarkedAsStale.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
        });
  }

  @Test
  void shouldStart() {
    var project = createTestProject(ProjectState.OFFERED);

    project.start();

    assertThat(project.getStatus().getState())
        .isEqualTo(ProjectState.RUNNING);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectStarted)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectStarted.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
        });
  }

  @Test
  void shouldEnd() {
    var project = createTestProject(ProjectState.RUNNING);

    project.complete();

    assertThat(project.getStatus().getState())
        .isEqualTo(ProjectState.COMPLETED);
    assertThat(project.getDomainEvents())
        .filteredOn(event -> event instanceof ProjectCompleted)
        .hasSize(1)
        .first()
        .isInstanceOfSatisfying(ProjectCompleted.class, event -> {
          assertThat(event.projectId()).isEqualTo(project.getId());
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
  void shouldNotRemoveLastSupervisor() {
    var supervisor = new Supervisor(UUID.randomUUID());
    var project = createProjectWithSupervisors(List.of(supervisor));

    assertThrows(RuntimeException.class, () -> project.removeSupervisor(supervisor));
  }

  @Test
  void shouldRemoveSupervisor() {
    var supervisorXavier = new Supervisor(UUID.randomUUID());
    var supervisorHomer = new Supervisor(UUID.randomUUID());
    var project = createProjectWithSupervisors(List.of(supervisorHomer, supervisorXavier));

    project.removeSupervisor(supervisorHomer);

    assertThat(project.getSupervisors()).doesNotContain(supervisorHomer);
  }

  @Test
  void shouldAddSupervisor() {
    var supervisorXavier = new Supervisor(UUID.randomUUID());
    var supervisorHomer = new Supervisor(UUID.randomUUID());
    var project = createProjectWithSupervisors(List.of(supervisorXavier));

    project.addSupervisor(supervisorHomer);

    assertThat(project.getSupervisors()).contains(supervisorHomer);
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
