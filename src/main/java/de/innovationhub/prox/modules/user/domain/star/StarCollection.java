package de.innovationhub.prox.modules.user.domain.star;

import de.innovationhub.prox.modules.commons.domain.AuditedAggregateRoot;
import de.innovationhub.prox.modules.user.domain.star.event.ProjectStarred;
import de.innovationhub.prox.modules.user.domain.star.event.ProjectUnstarred;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class StarCollection extends AuditedAggregateRoot {
  @Id
  private UUID id;

  private UUID userId;

  @ElementCollection
  private Set<UUID> starredProjects = new HashSet<>();

  public StarCollection(UUID userId) {
    this.id = UUID.randomUUID();
    this.userId = userId;
  }

  public void starProject(UUID projectId) {
    starredProjects.add(projectId);
    this.registerEvent(new ProjectStarred(this.id, this.userId, projectId));
  }

  public void unstarProject(UUID projectId) {
    starredProjects.remove(projectId);
    this.registerEvent(new ProjectUnstarred(this.id, this.userId, projectId));
  }
}
