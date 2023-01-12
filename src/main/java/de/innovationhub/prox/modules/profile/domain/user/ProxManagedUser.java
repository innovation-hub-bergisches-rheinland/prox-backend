package de.innovationhub.prox.modules.profile.domain.user;

import de.innovationhub.prox.modules.commons.domain.AbstractAggregateRoot;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerAvatarSet;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerRenamed;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerTagged;
import de.innovationhub.prox.modules.profile.domain.lecturer.events.LecturerVisibilityChanged;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// TODO: Revisit Inheritance strategy. The only reason I choose a mapped superclass was to not make
//      any changes to the database schema.
// @Inheritance(strategy = InheritanceType.)
// @Entity
@MappedSuperclass
public abstract class ProxManagedUser extends AbstractAggregateRoot {
  @Id
  private UUID id;
  private Boolean visibleInPublicSearch = false;

  @NotNull
  private UUID userId;

  private String name;


  @ElementCollection
  private Set<UUID> tags = new HashSet<>();

  private String avatarKey;

  public ProxManagedUser(UUID id, Boolean visibleInPublicSearch, UUID userId, String name) {
    this.id = id;
    this.visibleInPublicSearch = visibleInPublicSearch;
    this.userId = userId;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
    this.registerEvent(new LecturerTagged(this.id, this.tags));
  }

  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Lecturer title cannot be null");
    }
    if (this.name.equals(name)) {
      return;
    }

    this.name = name;
    this.registerEvent(new LecturerRenamed(this.id, this.name));
  }

  public void setAvatarKey(String avatarKey) {
    this.avatarKey = avatarKey;
    this.registerEvent(new LecturerAvatarSet(this.id, this.avatarKey));
  }

  public void setVisibleInPublicSearch(boolean visible) {
    this.visibleInPublicSearch = visible;
    this.registerEvent(new LecturerVisibilityChanged(this.id, this.visibleInPublicSearch));
  }
}
