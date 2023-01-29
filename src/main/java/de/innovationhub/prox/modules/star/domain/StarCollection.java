package de.innovationhub.prox.modules.star.domain;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.star.domain.event.ProjectStarred;
import de.innovationhub.prox.modules.star.domain.event.ProjectUnstarred;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(schema = PersistenceConfig.STAR_SCHEMA)
public class StarCollection extends AuditedAggregateRoot {
  @Id
  private UUID id;

  private UUID userId;

  @ElementCollection
  @CollectionTable(schema = PersistenceConfig.STAR_SCHEMA)
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
