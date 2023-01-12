package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectCreated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectInterestStated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectOffered;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectStateUpdated;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectTagged;
import de.innovationhub.prox.modules.project.domain.project.events.ProjectUpdated;
import de.innovationhub.prox.modules.project.domain.project.exception.ProjectStateException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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

  @Builder.Default
  @ElementCollection
  private List<Supervisor> supervisors = new ArrayList<>();

  @ElementCollection
  @Builder.Default
  private Set<UUID> tags = new HashSet<>();

  @ElementCollection
  @Builder.Default
  private Set<UUID> interestedUsers = new HashSet<>();

  public static Project create(
      Author author,
      String title,
      String summary,
      String description,
      String requirement,
      CurriculumContext context,
      @Nullable TimeBox timeBox,
      @Nullable UUID partner,
      Collection<UUID> supervisors
  ) {
    ProjectState state = supervisors.isEmpty() ? ProjectState.PROPOSED : ProjectState.OFFERED;
    var supervisorsList = supervisors
        .stream()
        .map(Supervisor::new)
        .toList();

    var project = new Project(UUID.randomUUID(),
        author, partner != null ? new Partner(partner) : null, title, summary, description, requirement, context,
        new ProjectStatus(state, Instant.now()), timeBox,
        supervisorsList, null, new HashSet<>());
    project.registerEvent(new ProjectCreated(project.getId()));
    return project;
  }

  public void update(
      String title,
      String summary,
      String description,
      String requirement,
      CurriculumContext context,
      @Nullable TimeBox timeBox,
      @Nullable UUID partner,
      Collection<UUID> supervisors
  ) {
    this.setTitle(title);
    this.setDescription(description);
    this.setSummary(summary);
    this.setRequirement(requirement);
    this.setCurriculumContext(context);
    this.setTimeBox(timeBox);
    this.setPartner(partner != null ? new Partner(partner) : null);
    this.setSupervisors(supervisors);

    this.registerEvent(new ProjectUpdated(this.getId()));
  }

  public void updateState(ProjectState state) {
    this.status.updateState(state);
    this.registerEvent(new ProjectStateUpdated(this.getId(), state));
  }

  public void applyCommitment(UUID supervisorId) {
    if(!this.status.canAcceptCommitment()) {
      throw new IllegalStateException("Project cannot accept commitment");
    }

    if(!this.supervisors.isEmpty()) {
      throw new IllegalStateException("Project already has a supervisor");
    }

    this.supervisors = new ArrayList<>();
    this.supervisors.add(new Supervisor(supervisorId));
    this.status.updateState(ProjectState.OFFERED);

    this.registerEvent(new ProjectOffered(this.id, supervisors));
  }

  public void setSupervisors(Collection<UUID> supervisors) {
    Objects.requireNonNull(supervisors);

    var supervisorList = supervisors.stream().map(Supervisor::new).toList();
    this.supervisors = new ArrayList<>(supervisorList);
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
    this.registerEvent(new ProjectTagged(this.id, tags));
  }

  public void stateInterest(UUID userId) {
    if (this.interestedUsers.contains(userId)) {
      return;
    }

    if (!this.status.canAcceptInterest()) {
      throw new ProjectStateException("Project cannot accept interest");
    }

    this.interestedUsers.add(userId);
    this.registerEvent(new ProjectInterestStated(this.id, userId));
  }
}
