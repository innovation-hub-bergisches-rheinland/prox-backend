package de.innovationhub.prox.project.project;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A project represents a course or a research project, that is in fact offered by a lecturer. A
 * project always belongs to a user, but can be created under the tenancy of an organization.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Project {

  private final UUID id;
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
  private ProjectStatus status;

  private TimeBox timeBox = null;

  private Set<UUID> supervisors = new HashSet<>();
  private Set<UUID> tags = new HashSet<>();

  public Project(UUID creatorId) {
    this.id = UUID.randomUUID();
    this.creatorId = creatorId;
  }

  public Project(UUID id, UUID creatorId, String title) {
    this.id = id;
    this.creatorId = creatorId;
    this.title = title;
  }

  public void addSupervisor(UUID supervisor) {
    this.supervisors.add(supervisor);
  }

  public void setTags(Collection<UUID> tag) {
    this.tags = new HashSet<>(tag);
  }
}
