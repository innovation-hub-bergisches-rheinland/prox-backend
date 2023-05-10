package de.innovationhub.prox.modules.user.domain.search;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesCreated;
import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesTagCollectionUpdated;
import de.innovationhub.prox.modules.user.domain.search.events.SearchPreferencesUpdated;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private ProjectSearch projectSearch = new ProjectSearch();

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private OrganizationSearch organizationSearch = new OrganizationSearch();

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private LecturerSearch lecturerSearch = new LecturerSearch();

  private UUID tagCollectionId;

  public static SearchPreferences create(
      UUID userId,
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
    searchPreferences.projectSearch = projectSearch;
    searchPreferences.organizationSearch = organizationSearch;
    searchPreferences.lecturerSearch = lecturerSearch;
    searchPreferences.registerEvent(new SearchPreferencesCreated(searchPreferences.getId(), searchPreferences.getUserId()));
    return searchPreferences;
  }

  public void update(
      ProjectSearch projectSearch,
      OrganizationSearch organizationSearch,
      LecturerSearch lecturerSearch
  ) {
    Objects.requireNonNull(projectSearch);
    Objects.requireNonNull(organizationSearch);
    Objects.requireNonNull(lecturerSearch);

    this.projectSearch = projectSearch;
    this.organizationSearch = organizationSearch;
    this.lecturerSearch = lecturerSearch;
    this.registerEvent(new SearchPreferencesUpdated(this.getId(), this.getUserId()));
  }

  public void setTagCollection(
      UUID tagCollectionId
  ) {
    Objects.requireNonNull(tagCollectionId);
    this.tagCollectionId = tagCollectionId;
    this.registerEvent(new SearchPreferencesTagCollectionUpdated(this.getId(), this.getUserId()));
  }
}
