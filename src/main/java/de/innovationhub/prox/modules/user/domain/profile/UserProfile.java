package de.innovationhub.prox.modules.user.domain.profile;

import de.innovationhub.prox.modules.commons.domain.AuditedAggregateRoot;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileTagged;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileAvatarSet;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileUpdated;
import de.innovationhub.prox.modules.user.domain.profile.exception.LecturerProfileAlreadyExistsException;
import de.innovationhub.prox.modules.user.domain.profile.exception.LecturerProfileDoesNotExistException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserProfile extends AuditedAggregateRoot {

  @Id
  private UUID id;

  @NotNull
  @NaturalId
  @Column(unique = true, updatable = false, nullable = false)
  private UUID userId;

  private String displayName;

  private String avatarKey;

  @OneToOne(cascade = CascadeType.ALL)
  private LecturerProfile lecturerProfile;

  protected UserProfile(UUID id, UUID userId, String displayName) {
    this.id = id;
    this.userId = userId;
    this.displayName = displayName;
  }

  public static UserProfile create(UUID userId, String displayName) {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(displayName);

    var profile = new UserProfile(UUID.randomUUID(), userId, displayName);
    profile.registerEvent(new UserProfileCreated(profile.id, profile.userId, profile.displayName));
    return profile;
  }

  public void update(String displayName) {
    this.displayName = displayName;
    registerEvent(new UserProfileUpdated(id, this.userId, displayName));
  }

  public UUID getId() {
    return id;
  }

  public void setAvatarKey(String avatarKey) {
    this.avatarKey = avatarKey;
    this.registerEvent(new UserProfileAvatarSet(this.id, this.avatarKey));
  }

  public void createLecturerProfile(Boolean visibleInPublicSearch,
      LecturerProfileInformation lecturerProfile) {
    if (this.lecturerProfile != null) {
      throw new LecturerProfileAlreadyExistsException("Lecturer profile already exists");
    }
    this.lecturerProfile = new LecturerProfile(this.id, visibleInPublicSearch,
        lecturerProfile);
    this.registerEvent(new LecturerProfileCreated(this.id, this.lecturerProfile.getId()));
  }

  public void updateLecturerProfile(Boolean visibleInPublicSearch,
      LecturerProfileInformation lecturerProfile) {
    if (this.lecturerProfile == null) {
      throw new LecturerProfileDoesNotExistException("Cannot update non-existing lecturer profile");
    }
    this.lecturerProfile.setVisibleInPublicSearch(visibleInPublicSearch);
    this.lecturerProfile.setProfile(lecturerProfile);
    this.registerEvent(new LecturerProfileUpdated(this.id, this.lecturerProfile.getId()));
  }

  public void tagLecturerProfile(Collection<UUID> tagIds) {
    if (this.lecturerProfile == null) {
      throw new LecturerProfileDoesNotExistException("Cannot tag a non-existing lecturer profile");
    }

    this.lecturerProfile.setTags(tagIds);
    this.registerEvent(new LecturerProfileTagged(this.id, this.lecturerProfile.getId(),
        this.lecturerProfile.getTags()));
  }
}
