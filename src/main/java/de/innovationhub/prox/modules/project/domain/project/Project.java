package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectCreated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectOffered;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectStateUpdated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
  @Builder.Default
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
  @Column(length = 10_000)
  private String summary;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(columnDefinition = "TEXT")
  private String requirement;

  @NotNull
  @OneToOne(cascade = CascadeType.ALL)
  private CurriculumContext curriculumContext;

  @NotNull
  @Setter(AccessLevel.PROTECTED)
  @Embedded
  private ProjectStatus status;

  @Builder.Default
  @Embedded
  private TimeBox timeBox = null;

  @Setter(AccessLevel.PROTECTED)
  @Builder.Default
  @ElementCollection
  private List<Supervisor> supervisors = new ArrayList<>();

  @ElementCollection
  @Builder.Default
  private Set<UUID> tags = new HashSet<>();

  public static Project create(
      Author author,
      String title,
      String summary,
      String description,
      String requirement,
      CurriculumContext context,
      @Nullable TimeBox timeBox,
      @Nullable UUID partner
  ) {
    var project = new Project(UUID.randomUUID(),
        author, partner != null ? new Partner(partner) : null, title, summary, description, requirement, context,
        new ProjectStatus(ProjectState.PROPOSED, Instant.now()), timeBox,
        new ArrayList<>(), null);
    project.registerEvent(new ProjectCreated(project.getId()));
    return project;
  }

  public void updateState(ProjectState state) {
    this.status.updateState(state);
    this.registerEvent(new ProjectStateUpdated(this.getId(), state));
  }

  public void applyCommitment(UUID supervisorId) {
    if(this.status.getState() != ProjectState.PROPOSED) {
      throw new IllegalStateException("Project is not in state PROPOSED");
    }

    if(!this.supervisors.isEmpty()) {
      throw new IllegalStateException("Project already has a supervisor");
    }

    this.supervisors = new ArrayList<>();
    this.supervisors.add(new Supervisor(supervisorId));
    this.status.updateState(ProjectState.OFFERED);

    this.registerEvent(new ProjectOffered(this.id, supervisors));
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

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
    this.registerEvent(new ProjectTagged(this.id, tags));
  }
}
