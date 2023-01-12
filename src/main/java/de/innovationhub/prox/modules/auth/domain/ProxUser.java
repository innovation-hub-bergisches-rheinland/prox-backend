package de.innovationhub.prox.modules.auth.domain;

import de.innovationhub.prox.modules.auth.domain.event.ProxUserRegistered;
import de.innovationhub.prox.modules.auth.domain.event.ProxUserStarredProject;
import de.innovationhub.prox.modules.auth.domain.event.ProxUserUnstarredProject;
import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
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
public class ProxUser extends AbstractAggregateRoot {
  @Id
  private UUID id;

  @ElementCollection
  private Set<UUID> starredProjects = new HashSet<>();

  public static ProxUser register(UUID id) {
    var user = new ProxUser(id);
    user.registerEvent(new ProxUserRegistered(id));
    return user;
  }

  private ProxUser(UUID id) {
    this.id = id;
  }

  public void starProject(UUID projectId) {
    starredProjects.add(projectId);
    this.registerEvent(new ProxUserStarredProject(this.id, projectId));
  }

  public void unstarProject(UUID projectId) {
    starredProjects.remove(projectId);
    this.registerEvent(new ProxUserUnstarredProject(this.id, projectId));
  }
}
