package de.innovationhub.prox.modules.user.domain.search;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(schema = PersistenceConfig.USER_SCHEMA)
public class SearchHistory extends AuditedAggregateRoot {

  private static final int MAX_SEARCHES = 10;

  @Id
  @Getter(AccessLevel.NONE)
  private UUID id;

  @NotNull
  @NaturalId
  @Column(unique = true, updatable = false, nullable = false)
  private UUID userId;

  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(schema = PersistenceConfig.USER_SCHEMA)
  private List<ProjectSearchEntry> projectSearches = new ArrayList<>();

  protected SearchHistory(UUID id, UUID userId) {
    this.id = id;
    this.userId = userId;
  }

  public static SearchHistory create(UUID userId) {
    Objects.requireNonNull(userId);
    return new SearchHistory(UUID.randomUUID(), userId);
  }

  public void addSearch(ProjectSearchEntry search) {
    Objects.requireNonNull(search);

    var existing = projectSearches.stream().filter(s -> s.searchEquals(search)).findFirst();
    existing.ifPresent(projectSearchEntry -> projectSearches.remove(projectSearchEntry));

    if (projectSearches.size() >= MAX_SEARCHES) {
      projectSearches.remove(0);
    }
    projectSearches.add(search);
  }
}
