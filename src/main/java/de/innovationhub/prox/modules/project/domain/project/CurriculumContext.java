package de.innovationhub.prox.modules.project.domain.project;

import de.innovationhub.prox.config.PersistenceConfig;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A CurriculumContext is the context in which a project should be carried out. For example, a
 * project can be offered as a interdisciplinary project so that both engineering and computer
 * science students can participate. Or it is suited for both a master thesis or a bachelor thesis.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(schema = PersistenceConfig.PROJECT_SCHEMA)
public class CurriculumContext {

  public static final CurriculumContext EMPTY =
      new CurriculumContext(new ArrayList<>(), new ArrayList<>());

  @Id
  private final UUID id = UUID.randomUUID();

  @ElementCollection
  @CollectionTable(schema = PersistenceConfig.PROJECT_SCHEMA)
  private List<String> disciplines;

  @ElementCollection
  @CollectionTable(schema = PersistenceConfig.PROJECT_SCHEMA)
  private List<String> moduleTypes;

  public CurriculumContext(List<String> disciplines, List<String> moduleTypes) {
    this.disciplines = disciplines;
    this.moduleTypes = moduleTypes;
  }
}
