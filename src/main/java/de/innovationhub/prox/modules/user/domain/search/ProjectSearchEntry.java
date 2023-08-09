package de.innovationhub.prox.modules.user.domain.search;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.project.domain.project.ProjectState;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(schema = PersistenceConfig.USER_SCHEMA)
@AllArgsConstructor
public class ProjectSearchEntry extends AuditedAggregateRoot {

  @Id
  private UUID id;

  private String text;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = PersistenceConfig.USER_SCHEMA)
  private List<UUID> tags;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = PersistenceConfig.USER_SCHEMA)
  @Enumerated(EnumType.STRING)
  private List<ProjectState> states;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = PersistenceConfig.USER_SCHEMA)
  private List<String> moduleTypes;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(schema = PersistenceConfig.USER_SCHEMA)
  private List<String> disciplines;

  public static ProjectSearchEntry create(String text, Collection<UUID> tags,
      Collection<ProjectState> states,
      Collection<String> moduleTypes, Collection<String> disciplines) {
    return new ProjectSearchEntry(
        UUID.randomUUID(),
        text,
        newListOrNull(tags),
        newListOrNull(states),
        newListOrNull(moduleTypes),
        newListOrNull(disciplines)
    );
  }

  private static <T> ArrayList<T> newListOrNull(Collection<T> collection) {
    return collection == null ? null : new ArrayList<>(collection);
  }

  // Note that this is not the same as equals()!
  // This method checks whether the search parameters are the same, but ignores the id.
  public boolean searchEquals(ProjectSearchEntry other) {
    return Objects.equals(text, other.text) && Objects.equals(tags, other.tags)
        && Objects.equals(states, other.states) && Objects.equals(moduleTypes,
        other.moduleTypes) && Objects.equals(disciplines, other.disciplines);
  }
}
