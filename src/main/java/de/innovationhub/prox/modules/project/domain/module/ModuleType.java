package de.innovationhub.prox.modules.project.domain.module;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(schema = PersistenceConfig.PROJECT_SCHEMA)
public class ModuleType extends AuditedAggregateRoot {

  @Id
  private String key;
  private String name;

  @ElementCollection
  @CollectionTable(schema = PersistenceConfig.PROJECT_SCHEMA)
  private List<String> disciplines;

  private boolean active = true;

  public ModuleType(String key, String name, List<String> disciplines) {
    this.key = key;
    this.name = name;
    this.disciplines = disciplines;
  }
}
