package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
  private UUID id;

  @NotNull
  private UUID creatorId;

  private UUID organizationId;

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
  @OneToOne
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
  @ManyToMany
  private List<Supervisor> supervisors = new ArrayList<>();

  @Builder.Default
  @ElementCollection
  private List<UUID> tags = new ArrayList<>();

  public void archive() {
    this.status.updateState(ProjectState.ARCHIVED);
  }

  public void unarchive() {
    this.status.updateState(ProjectState.PROPOSED);
  }

  public void stale() {
    this.status.updateState(ProjectState.STALE);
  }

  public void offer(Supervisor supervisor) {
    Objects.requireNonNull(supervisor);

    this.status.updateState(ProjectState.OFFERED);
    this.supervisors = new ArrayList<>();
    this.supervisors.add(supervisor);
  }

  public void start() {
    this.status.updateState(ProjectState.RUNNING);
  }

  public void complete() {
    this.status.updateState(ProjectState.COMPLETED);
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

  public void setTags(Collection<UUID> tag) {
    this.tags = new ArrayList<>(tag);
  }
}
