package de.innovationhub.prox.modules.user.domain.search;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesCreated;
import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesUpdated;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(schema = PersistenceConfig.USER_SCHEMA)
public class SearchPreferences extends AuditedAggregateRoot {
  @Id
  private UUID id;

  @Column(unique = true, updatable = false, nullable = false)
  private UUID userId;

  @Embedded
  private ProjectSearch projectSearch = new ProjectSearch();

  @Embedded
  private OrganizationSearch organizationSearch = new OrganizationSearch();

  @Embedded
  private LecturerSearch lecturerSearch = new LecturerSearch();

  private UUID tagCollectionId;

  public static SearchPreferences create(
      UUID userId,
      @Nullable UUID tagCollectionId,
      ProjectSearch projectSearch,
      OrganizationSearch organizationSearch,
      LecturerSearch lecturerSearch
  ) {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(projectSearch);
    Objects.requireNonNull(organizationSearch);
    Objects.requireNonNull(lecturerSearch);

    SearchPreferences searchPreferences = new SearchPreferences();
    searchPreferences.id = UUID.randomUUID();
    searchPreferences.userId = userId;
    searchPreferences.tagCollectionId = tagCollectionId;
    searchPreferences.projectSearch = projectSearch;
    searchPreferences.organizationSearch = organizationSearch;
    searchPreferences.lecturerSearch = lecturerSearch;
    searchPreferences.registerEvent(new SearchPreferencesCreated(searchPreferences.getId(), searchPreferences.getUserId()));
    return searchPreferences;
  }

  public void update(
      @Nullable UUID tagCollectionId,
      ProjectSearch projectSearch,
      OrganizationSearch organizationSearch,
      LecturerSearch lecturerSearch
  ) {
    Objects.requireNonNull(projectSearch);
    Objects.requireNonNull(organizationSearch);
    Objects.requireNonNull(lecturerSearch);

    this.tagCollectionId = tagCollectionId;
    this.projectSearch = projectSearch;
    this.organizationSearch = organizationSearch;
    this.lecturerSearch = lecturerSearch;
    this.registerEvent(new SearchPreferencesUpdated(this.getId(), this.getUserId()));
  }
}
