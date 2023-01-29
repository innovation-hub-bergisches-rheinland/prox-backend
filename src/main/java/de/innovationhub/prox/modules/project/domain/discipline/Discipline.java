package de.innovationhub.prox.modules.project.domain.discipline;

import de.innovationhub.prox.commons.buildingblocks.AuditedAggregateRoot;
import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(schema = PersistenceConfig.PROJECT_SCHEMA)
public class Discipline extends AuditedAggregateRoot {

  @Id
  private String key;
  private String name;

  public Discipline(String key, String name) {
    this.key = key;
    this.name = name;
  }
}
