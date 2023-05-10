package de.innovationhub.prox.modules.user.domain.search;

import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(schema = PersistenceConfig.USER_SCHEMA)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
public class ProjectSearch {
  @Id
  private UUID id = UUID.randomUUID();

  private Boolean enabled = false;

  @ElementCollection
  @CollectionTable(schema = PersistenceConfig.USER_SCHEMA)
  private Set<String> moduleTypes = new HashSet<>();

  @ElementCollection
  @CollectionTable(schema = PersistenceConfig.USER_SCHEMA)
  private Set<String> disciplines = new HashSet<>();

  public ProjectSearch(Boolean enabled, Set<String> moduleTypes, Set<String> disciplines) {
    this.enabled = enabled;
    this.moduleTypes = moduleTypes;
    this.disciplines = disciplines;
  }
}
