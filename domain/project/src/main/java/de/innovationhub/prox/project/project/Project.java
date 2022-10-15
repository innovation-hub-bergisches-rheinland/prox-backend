package de.innovationhub.prox.project.project;

import de.innovationhub.prox.project.project.exception.InvalidProjectStateTransitionException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A project represents a course or a research project, that is in fact offered by a lecturer. A
 * project always belongs to a user, but can be created under the tenancy of an organization.
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
public class Project {

  @NotNull
  private final UUID id;

  @NotNull
  private final UUID creatorId;

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
  private CurriculumContext curriculumContext;

  @NotNull
  @Setter(AccessLevel.PROTECTED)
  private ProjectStatus status;

  @Setter(AccessLevel.PROTECTED)
  @Builder.Default
  private TimeBox timeBox = null;

  @Setter(AccessLevel.PROTECTED)
  @Builder.Default
  private Set<UUID> supervisors = new HashSet<>();

  @Builder.Default
  private Set<UUID> tags = new HashSet<>();

  public void archive() {
    if(this.status.getState() != ProjectState.PROPOSED) {
      throw new InvalidProjectStateTransitionException("Can only archive projects in proposed state");
    }
    this.status.updateState(ProjectState.ARCHIVED);
  }

  public void unarchive() {
    if(this.status.getState() != ProjectState.ARCHIVED) {
      throw new InvalidProjectStateTransitionException("Can only unarchive projects in archived state");
    }
    this.status.updateState(ProjectState.PROPOSED);
  }

  public void stale() {
    if(this.status.getState() != ProjectState.ARCHIVED) {
      throw new InvalidProjectStateTransitionException("Can only stale projects in archived state");
    }
    this.status.updateState(ProjectState.STALE);
  }

  public void offer(UUID supervisor) {
    if(this.status.getState() != ProjectState.PROPOSED) {
      throw new InvalidProjectStateTransitionException("Can only offer projects in proposed state");
    }

    this.supervisors = new HashSet<>();
    this.supervisors.add(supervisor);
    this.status.updateState(ProjectState.OFFERED);
  }

  public void start() {
    if(this.status.getState() != ProjectState.OFFERED) {
      throw new InvalidProjectStateTransitionException("Can only start projects in offered state");
    }

    this.status.updateState(ProjectState.RUNNING);
  }

  public void complete() {
    if(this.status.getState() != ProjectState.RUNNING) {
      throw new InvalidProjectStateTransitionException("Can only complete projects in running state");
    }

    this.status.updateState(ProjectState.COMPLETED);
  }

  public void addSupervisor(UUID supervisor) {
    this.supervisors.add(supervisor);
  }

  public void removeSupervisor(UUID supervisor) {
    if(!this.supervisors.contains(supervisor)) return;

    if(this.supervisors.size() == 1) {
      throw new RuntimeException("There must be at least one supervisor");
    }

    this.supervisors.remove(supervisor);
  }

  public void setTags(Collection<UUID> tag) {
    this.tags = new HashSet<>(tag);
  }
}
