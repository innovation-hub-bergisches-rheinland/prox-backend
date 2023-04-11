package de.innovationhub.prox.modules.user.domain.profile;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.LecturerProfileUpdated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileAvatarSet;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileCreated;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileTagged;
import de.innovationhub.prox.modules.user.domain.profile.events.UserProfileUpdated;
import de.innovationhub.prox.modules.user.domain.profile.exception.LecturerProfileAlreadyExistsException;
import de.innovationhub.prox.modules.user.domain.profile.exception.LecturerProfileDoesNotExistException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(schema = PersistenceConfig.USER_SCHEMA)
//@Where(clause = "visible_in_public_search = true")
public class UserProfile extends AuditedAggregateRoot {

  @Id
  @Getter(AccessLevel.NONE)
  private UUID id;

  private Boolean visibleInPublicSearch;

  @NotNull
  @NaturalId
  @Column(unique = true, updatable = false, nullable = false)
  private UUID userId;

  private String displayName;

  private String avatarKey;

  @Embedded
  private ContactInformation contactInformation;

  @OneToOne(cascade = CascadeType.ALL)
  private LecturerProfile lecturerProfile;

  @Column(columnDefinition = "TEXT")
  private String vita;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = PersistenceConfig.USER_SCHEMA)
  private Set<UUID> tags = new HashSet<>();

  protected UserProfile(UUID id, UUID userId, String displayName, String vita,
      ContactInformation contactInformation, boolean visibleInPublicSearch) {
    this.id = id;
    this.userId = userId;
    this.displayName = displayName;
    this.vita = vita;
    this.contactInformation = contactInformation;
    this.visibleInPublicSearch = visibleInPublicSearch;
  }

  public static UserProfile create(UUID userId, String displayName, String vita,
      ContactInformation contactInformation, boolean visibleInPublicSearch) {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(displayName);

    var profile = new UserProfile(UUID.randomUUID(), userId, displayName, vita, contactInformation,
        visibleInPublicSearch);
    profile.registerEvent(
        new UserProfileCreated(profile.id, profile.userId, profile.displayName, profile.vita,
            profile.contactInformation));
    return profile;
  }

  public void update(String displayName, String vita, ContactInformation contactInformation,
      boolean visibleInPublicSearch) {
    this.displayName = displayName;
    this.vita = vita;
    this.contactInformation = contactInformation;
    this.visibleInPublicSearch = visibleInPublicSearch;
    registerEvent(new UserProfileUpdated(id, this.userId, displayName, vita, contactInformation));
  }

  public void setAvatarKey(String avatarKey) {
    this.avatarKey = avatarKey;
    this.registerEvent(new UserProfileAvatarSet(this.id, this.avatarKey));
  }

  public void createLecturerProfile(LecturerProfileInformation lecturerProfile) {
    if (this.lecturerProfile != null) {
      throw new LecturerProfileAlreadyExistsException("Lecturer profile already exists");
    }
    this.lecturerProfile = new LecturerProfile(this.id,
        lecturerProfile);
    this.registerEvent(new LecturerProfileCreated(this.id, this.lecturerProfile.getId()));
  }

  public void updateLecturerProfile(LecturerProfileInformation lecturerProfile) {
    if (this.lecturerProfile == null) {
      throw new LecturerProfileDoesNotExistException("Cannot update non-existing lecturer profile");
    }
    this.lecturerProfile.setProfile(lecturerProfile);
    this.registerEvent(new LecturerProfileUpdated(this.id, this.lecturerProfile.getId()));
  }

  public void tagProfile(Collection<UUID> tagIds) {
    this.tags = new HashSet<>(tagIds);
    this.registerEvent(new UserProfileTagged(this.id, this.getTags()));
  }
}
