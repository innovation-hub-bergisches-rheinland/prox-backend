package de.innovationhub.prox.modules.project;

import de.innovationhub.prox.modules.project.domain.module.ModuleType;
import java.util.List;

public class ModuleTypeFixtures {
  public static final ModuleType BACHELOR_THESIS = new ModuleType(
      "BA",
      "Bachelor Thesis",
      List.of(DisciplineFixtures.INF, DisciplineFixtures.ING)
  );

  public static final List<ModuleType> ALL = List.of(BACHELOR_THESIS);
}
