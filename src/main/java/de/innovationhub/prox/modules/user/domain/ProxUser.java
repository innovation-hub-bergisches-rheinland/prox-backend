package de.innovationhub.prox.modules.user.domain;

import de.innovationhub.prox.modules.commons.domain.AuditedAggregateRoot;
import de.innovationhub.prox.modules.user.domain.lecturer.events.LecturerAvatarSet;
import de.innovationhub.prox.modules.user.domain.lecturer.events.LecturerRenamed;
import de.innovationhub.prox.modules.user.domain.lecturer.events.LecturerTagged;
import de.innovationhub.prox.modules.user.domain.lecturer.events.LecturerVisibilityChanged;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@DiscriminatorColumn(name = "profile_type")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class ProxUser extends AuditedAggregateRoot {

  @Id
  private UUID id;
  private Boolean visibleInPublicSearch = false;

  @NotNull
  private UUID userId;

  private String displayName;

  @ElementCollection
  private Set<UUID> tags = new HashSet<>();

  private String avatarKey;

  protected ProxUser(UUID id, Boolean visibleInPublicSearch, UUID userId, String displayName) {
    this.id = id;
    this.visibleInPublicSearch = visibleInPublicSearch;
    this.userId = userId;
    this.displayName = displayName;
  }

  public UUID getId() {
    return id;
  }

  public void setTags(Collection<UUID> tags) {
    this.tags = new HashSet<>(tags);
    this.registerEvent(new LecturerTagged(this.id, this.tags));
  }

  public void setDisplayName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Lecturer title cannot be null");
    }
    if (this.displayName.equals(name)) {
      return;
    }

    this.displayName = name;
    this.registerEvent(new LecturerRenamed(this.id, this.displayName));
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
