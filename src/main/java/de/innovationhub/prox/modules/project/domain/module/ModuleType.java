package de.innovationhub.prox.modules.project.domain.module;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import de.innovationhub.prox.modules.project.domain.discipline.Discipline;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(schema = PersistenceConfig.PROJECT_SCHEMA)
  private List<Discipline> disciplines;

  private boolean active = true;

  public ModuleType(String key, String name, List<Discipline> disciplines) {
    this.key = key;
    this.name = name;
    this.disciplines = disciplines;
  }
}
