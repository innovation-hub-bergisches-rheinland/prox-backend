package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * A project represents a course or a research project, that is in fact offered by a lecturer. A
 * project always belongs to a user, but can be created under the tenancy of an organization.
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Project extends AbstractAggregateRoot {

  @Id
  private UUID id = UUID.randomUUID();

  @NotNull
  @Embedded
  private Author author;

  @Embedded
  private Partner partner;

  @NotBlank
  @Size(max = 255)
  private String title;

  @NotBlank
  @Size(max = 10_000)
  private String summary;

  @Size(max = 10_000)
  private String description;

  @Size(max = 10_000)
  private String requirement;

  @NotNull
  @OneToOne(cascade = CascadeType.ALL)
  private CurriculumContext curriculumContext;

  @NotNull
  @Setter(AccessLevel.PROTECTED)
  @Embedded
  private ProjectStatus status;

  @Setter(AccessLevel.PROTECTED)
  @Builder.Default
  @Embedded
  private TimeBox timeBox = null;

  @Setter(AccessLevel.PROTECTED)
  @Builder.Default
  @ElementCollection
  private List<Supervisor> supervisors = new ArrayList<>();

  @ElementCollection
  private Set<UUID> tags = new HashSet<>();

  public static Project create(
      Author author,
      String title,
      String summary,
      String description,
      String requirement,
      CurriculumContext context,
      @Nullable TimeBox timeBox
  ) {
    var project = new Project(UUID.randomUUID(),
        author, null, title, summary, description, requirement, context,
        new ProjectStatus(ProjectState.PROPOSED, Instant.now()), timeBox,
        new ArrayList<>(), null);
    project.registerEvent(new ProjectCreated(project.getId()));
    return project;
  }

  public void archive() {
    this.status.updateState(ProjectState.ARCHIVED);
    this.registerEvent(new ProjectArchived(this.id));
  }

  public void unarchive() {
    this.status.updateState(ProjectState.PROPOSED);
    this.registerEvent(new ProjectUnarchived(this.id));
  }

  public void stale() {
    this.status.updateState(ProjectState.STALE);
    this.registerEvent(new ProjectMarkedAsStale(this.id));
  }

  public void offer(Supervisor supervisor) {
    Objects.requireNonNull(supervisor);

    offer(List.of(supervisor));
  }

  public void offer(List<Supervisor> supervisors) {
    Objects.requireNonNull(supervisors);
    if (supervisors.isEmpty()) {
      throw new RuntimeException("Cannot offer without any supervisor");
    }

    this.status.updateState(ProjectState.OFFERED);
    this.supervisors = new ArrayList<>(supervisors);
    this.registerEvent(new ProjectOffered(this.id, supervisors));
  }

  public void start() {
    this.status.updateState(ProjectState.RUNNING);
    this.registerEvent(new ProjectStarted(this.id));
  }

  public void complete() {
    this.status.updateState(ProjectState.COMPLETED);
    this.registerEvent(new ProjectCompleted(this.id));
  }

  public void addSupervisor(Supervisor supervisor) {
    this.supervisors.add(supervisor);
  }

  public void removeSupervisor(Supervisor supervisor) {
    if (!this.supervisors.contains(supervisor)) {
      return;
    }

    if (this.supervisors.size() == 1) {
      throw new RuntimeException("There must be at least one supervisor");
    }

    this.supervisors.remove(supervisor);
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
    this.registerEvent(new ProjectTagged(this.id, tags));
  }

  public void setTimeBox(TimeBox timeBox) {
    this.timeBox = timeBox;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
    this.registerEvent(new ProjectPartnered(this.id, partner.getOrganizationId()));
  }
}
