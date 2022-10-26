package de.innovationhub.prox.modules.project.application.module.usecase;

import de.innovationhub.prox.modules.commons.application.usecase.UseCase;
import java.util.List;

public record FindModulesByDisciplines(List<String> disciplineKeys) implements UseCase {
  public FindModulesByDisciplines {
    if (disciplineKeys == null) {
      throw new IllegalArgumentException("disciplineKeys must not be null");
    }
  }
}
